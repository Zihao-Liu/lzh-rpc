package com.lzh.rpc.demo.provider.config;

import com.lzh.rpc.core.model.server.ServerProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Liuzihao
 */
@Configuration
public class ServerConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "rpc-server")
    public ServerProperty serverProperty() {
        return new ServerProperty();
    }

}
