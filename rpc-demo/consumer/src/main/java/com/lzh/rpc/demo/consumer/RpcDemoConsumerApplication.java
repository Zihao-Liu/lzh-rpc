package com.lzh.rpc.demo.consumer;

import com.lzh.rpc.core.consumer.factory.BaseConsumerFactory;
import com.lzh.rpc.core.consumer.factory.RpcSpringConsumerFactory;
import com.lzh.rpc.demo.consumer.config.ConsumerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages = {"com.lzh.rpc"})
public class RpcDemoConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDemoConsumerApplication.class, args);
    }

    @Resource
    private ConsumerConfiguration consumerConfiguration;

    @Bean
    public BaseConsumerFactory providerFactory() {
        return new RpcSpringConsumerFactory(consumerConfiguration.getConsumers());
    }
}
