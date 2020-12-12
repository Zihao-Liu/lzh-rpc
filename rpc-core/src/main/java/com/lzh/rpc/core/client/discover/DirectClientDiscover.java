package com.lzh.rpc.core.client.discover;

import static com.lzh.rpc.common.constant.CommonConstant.COMMON_SPLIT;
import static com.lzh.rpc.core.constant.RegisterTypeEnum.DIRECT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.core.client.factory.BaseClientFactory;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.client.ClientProperty;

/**
 * Direct: IP直连方式，适合在测试/联调环境中使用。不需要配置注册中心，只需要配置server端的ip:host即可使用
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class DirectClientDiscover extends AbstractClientDiscover {
    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(DirectClientDiscover.class);

    private static final DirectClientDiscover INSTANCE = new DirectClientDiscover();

    public static RpcClientDiscover getInstance() {
        return INSTANCE;
    }

    @Override
    public void doDestroy() {
        LOGGER.info("discover destroy");
    }

    @Override
    public void doDiscover(List<ClientProperty> clientPropertyList) {
        clientPropertyList.stream()
                .filter(property -> StringUtils.isBlank(property.getRegister())
                        || DIRECT.getType().equalsIgnoreCase(property.getRegister()))
                .forEach(source -> {
                    List<String> addressList = Arrays.asList(source.getDomain().split(COMMON_SPLIT));
                    List<ProviderInstance> sourceInfoList = addressList.stream()
                            .map(ProviderInstance::new)
                            .collect(Collectors.toList());
                    BaseClientFactory.updateProvider(source.getReference(), sourceInfoList);
                });
    }
}
