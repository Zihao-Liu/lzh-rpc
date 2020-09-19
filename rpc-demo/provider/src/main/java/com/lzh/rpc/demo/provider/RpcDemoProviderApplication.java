package com.lzh.rpc.demo.provider;

import com.lzh.rpc.common.model.provider.ProviderProperty;
import com.lzh.rpc.core.provider.factory.BaseProviderFactory;
import com.lzh.rpc.core.provider.factory.RpcSpringProviderFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootApplication
public class RpcDemoProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDemoProviderApplication.class, args);
    }

    @Resource
    private ProviderProperty providerProperty;

    @Bean
    public BaseProviderFactory providerFactory() {
        return new RpcSpringProviderFactory(providerProperty);
    }
}
