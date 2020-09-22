package com.lzh.rpc.core.provider.factory;

import com.google.common.collect.Maps;
import com.lzh.rpc.common.annotation.RpcProvider;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.provider.ProviderProperty;
import com.lzh.rpc.core.provider.register.AbstractProviderRegister;
import com.lzh.rpc.core.provider.server.NettyServer;
import org.apache.commons.collections4.MapUtils;
import org.springframework.lang.Nullable;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

import static com.lzh.rpc.common.constant.CommonConstant.BANNER_INFO;

/**
 * Provider中提供服务的service通过该类注册到RPC框架中。这是一个抽象类，提供了基于Spring框架和
 * 无框架模式下的两种注册方式，可以根据需要选择合适的方式。具体使用方法，查看文档
 * TODO Lzh 补充文档链接
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public abstract class BaseProviderFactory {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(BaseProviderFactory.class);
    /**
     * 用来保存注册到RPC框架中提供服务的Service实现
     */
    private static final Map<Class<?>, Object> SERVICE_MAP = Maps.newHashMap();

    /* ------------------------------------------------- 子类使用方法 ------------------------------------------------*/

    static void putService(String name, Object instance, @Nullable RpcProvider rpcProvider) {
        if (Objects.isNull(rpcProvider)) {
            LOGGER.debug("annotation is null, name: [{}], class: [{}]", name, instance);
            return;
        }
        putService(name, instance, rpcProvider.value());
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

    abstract ProviderProperty getProperty();


    public void start() throws UnknownHostException {
        ProviderProperty providerProperty = this.getProperty();
        if (providerProperty.getEnable()) {
            LOGGER.info("{}", BANNER_INFO);
        }
        AbstractProviderRegister.getRegister(providerProperty).doRegister();
        new NettyServer().startNetty(providerProperty);
    }

    public void stop() throws UnknownHostException {
        ProviderProperty providerProperty = this.getProperty();
        AbstractProviderRegister.getRegister(providerProperty).doDestroy();
    }
}
