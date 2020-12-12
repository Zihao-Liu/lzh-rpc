package com.lzh.rpc.core.client.discover;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.core.constant.RegisterTypeEnum;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.client.ClientProperty;


/**
 * client服务发现抽象类，默认提供了两种服务发现方式
 * <p>
 * 1. Direct: IP直连方式，适合在测试/联调环境中使用。不需要配置注册中心，只需要配置server端的ip:host即可使用
 * 2. HTTP: 注册中心，使用注册中心进行服务发现。server端必须在注册中心注册
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public abstract class AbstractClientDiscover implements RpcClientDiscover {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(AbstractClientDiscover.class);

    public static void discover(List<ClientProperty> clientPropertyList) {
        Map<String, List<ClientProperty>> clientMap = clientPropertyList.stream()
                .collect(Collectors.groupingBy(ClientProperty::getRegister));

        clientMap.forEach((type, properties) -> {
            RegisterTypeEnum discoverType = RegisterTypeEnum.typeOf(type);

            if (Objects.isNull(discoverType)) {
                LOGGER.error("discover type: [{}] not exist", type);
                throw RpcException.error("discover type: [%s] not exist", type);
            }
            discoverType.getDiscover().doDiscover(properties);
        });
    }

    public static void destroy() {
        for (RegisterTypeEnum type : RegisterTypeEnum.values()) {
            type.getDiscover().doDestroy();
        }
    }
}
