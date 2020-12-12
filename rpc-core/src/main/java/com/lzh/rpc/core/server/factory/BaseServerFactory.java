package com.lzh.rpc.core.server.factory;

import static com.lzh.rpc.common.constant.CommonConstant.BANNER_INFO;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import com.google.common.collect.Maps;
import com.lzh.rpc.core.annotation.RpcServer;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.server.ServerProperty;
import com.lzh.rpc.core.server.register.AbstractServerRegister;
import com.lzh.rpc.core.server.net.NettyServer;

/**
 * Provider中提供服务的service通过该类注册到RPC框架中。这是一个抽象类，提供了基于Spring框架和
 * 无框架模式下的两种注册方式，可以根据需要选择合适的方式。具体使用方法，查看文档
 * TODO Lzh 补充文档链接
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public abstract class BaseServerFactory {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(BaseServerFactory.class);
    /**
     * 用来保存注册到RPC框架中提供服务的Service实现
     */
    private static final Map<Class<?>, Object> SERVICE_MAP = Maps.newHashMap();

    /* ------------------------------------------------- 子类使用方法 ------------------------------------------------*/

    static void putService(String name, Object instance, @Nullable RpcServer rpcProvider) {
        if (Objects.isNull(rpcProvider)) {
            LOGGER.debug("annotation is null, name: [{}], class: [{}]", name, instance);
            return;
        }
        String referenceName = name;
        if (StringUtils.isNotBlank(rpcProvider.name())) {
            referenceName = rpcProvider.name();
        }
        for (Class<?> type : rpcProvider.types()) {
            putService(referenceName, instance, type);
        }
    }

    static void putService(String name, Object instance, Class<?> clazz) {
        LOGGER.debug("provider service registered, name: [{}], instance [{}], class [{}]", name, instance, clazz);
        SERVICE_MAP.put(clazz, instance);
    }

    static boolean isEmpty() {
        return MapUtils.isEmpty(SERVICE_MAP);
    }

    static int size() {
        return SERVICE_MAP.size();
    }

    @Nullable
    public static Object getService(Class<?> serviceClass) {
        return SERVICE_MAP.get(serviceClass);
    }

    abstract ServerProperty getProperty();


    public void start() throws UnknownHostException {
        ServerProperty serverProperty = this.getProperty();
        if (serverProperty.getEnable()) {
            LOGGER.info("{}", BANNER_INFO);
        } else {
            LOGGER.info("skip start rpc server");
            return;
        }
        AbstractServerRegister.getRegister(serverProperty).doRegister(serverProperty);
        new NettyServer().startNetty(serverProperty);
    }

    public void stop() throws UnknownHostException {
        ServerProperty serverProperty = this.getProperty();
        AbstractServerRegister.getRegister(serverProperty).doDestroy(serverProperty);
    }
}
