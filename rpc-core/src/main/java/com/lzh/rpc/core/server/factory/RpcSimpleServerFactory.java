package com.lzh.rpc.core.server.factory;

import  com.lzh.rpc.core.model.server.ServerProperty;

/**
 * 使用非框架的方式注册Rpc Provider Service
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class RpcSimpleServerFactory extends BaseServerFactory {

    private final ServerProperty serverProperty;

    public RpcSimpleServerFactory(ServerProperty serverProperty) {
        this.serverProperty = serverProperty;
    }

    public RpcSimpleServerFactory addService(Class<?> clazz, Object instance) {
        BaseServerFactory.putService(clazz.getSimpleName(), instance, clazz);
        return this;
    }

    @Override
    ServerProperty getProperty() {
        return this.serverProperty;
    }
}
