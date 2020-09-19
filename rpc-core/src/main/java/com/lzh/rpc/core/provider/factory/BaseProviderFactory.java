package com.lzh.rpc.core.provider.factory;

import com.google.common.collect.Maps;
import com.lzh.rpc.common.annotation.RpcProvider;
import com.lzh.rpc.common.model.provider.ProviderProperty;
import com.lzh.rpc.core.log.LoggerAdapter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * @author Liuzihao
 * @since 0.0.1
 */
public abstract class BaseProviderFactory {
    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(BaseProviderFactory.class);
    private static final Map<Class<?>, Object> SERVICE_MAP = Maps.newHashMap();

    static ProviderProperty providerProperty;

    protected static void setProviderProperty(ProviderProperty providerProperty) {
        BaseProviderFactory.providerProperty = providerProperty;
    }

    public static ProviderProperty getProviderProperty() {
        return providerProperty;
    }

    static void putService(String name, Object instance, @Nullable RpcProvider rpcProvider) {
        if (Objects.isNull(rpcProvider)) {
            LOGGER.debug("annotation is null, name: [{}], class: [{}]", name, instance);
            return;
        }
        LOGGER.debug("provider service registered, name: [{}], instance [{}], class [{}]", name, instance, rpcProvider.value());
        SERVICE_MAP.put(rpcProvider.value(), instance);
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
}
