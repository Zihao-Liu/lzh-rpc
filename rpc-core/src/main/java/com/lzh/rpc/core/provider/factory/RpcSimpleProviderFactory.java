package com.lzh.rpc.core.provider.factory;

import  com.lzh.rpc.core.model.provider.ProviderProperty;

/**
 * 使用非框架的方式注册Rpc Provider Service
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class RpcSimpleProviderFactory extends BaseProviderFactory {

    private ProviderProperty providerProperty;

    public RpcSimpleProviderFactory(ProviderProperty providerProperty) {
        this.providerProperty = providerProperty;
    }

    public RpcSimpleProviderFactory addService(Class<?> clazz, Object instance) {
        BaseProviderFactory.putService(clazz.getSimpleName(), instance, clazz);
        return this;
    }

    @Override
    ProviderProperty getProperty() {
        return this.providerProperty;
    }
}
