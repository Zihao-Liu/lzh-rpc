package com.lzh.rpc.demo.provider.config;

import com.lzh.rpc.common.model.provider.ProviderProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfuguration {

    @Bean
    @ConfigurationProperties(prefix = "lzh.rpc.provider")
    public ProviderProperty providerProperty() {
        return new ProviderProperty();
    }

}
