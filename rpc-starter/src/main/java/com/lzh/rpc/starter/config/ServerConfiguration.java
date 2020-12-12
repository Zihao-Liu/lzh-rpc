package com.lzh.rpc.starter.config;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lzh.rpc.core.model.server.ServerProperty;
import com.lzh.rpc.core.server.factory.BaseServerFactory;
import com.lzh.rpc.core.server.factory.RpcSpringServerFactory;

@Configuration
public class ServerConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "rpc-server2")
    public ServerProperty serverProperty() {
        return new ServerProperty();
    }

//    @Resource
//    private ServerProperty serverProperty;

    @Bean
    public BaseServerFactory providerFactory() {
        return new RpcSpringServerFactory(this.serverProperty());
    }
}
