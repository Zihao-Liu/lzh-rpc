package com.lzh.rpc.demo.consumer;

import com.lzh.rpc.core.client.factory.BaseClientFactory;
import com.lzh.rpc.core.client.factory.RpcSpringClientFactory;
import com.lzh.rpc.demo.consumer.config.ClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author Liuzihao
 */
@SpringBootApplication(scanBasePackages = {"com.lzh"})
public class RpcDemoConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDemoConsumerApplication.class, args);
    }

    @Resource
    private ClientConfiguration clientConfiguration;

    @Bean
    public BaseClientFactory clientFactory() {
        return new RpcSpringClientFactory(clientConfiguration.getClients());
    }
}
