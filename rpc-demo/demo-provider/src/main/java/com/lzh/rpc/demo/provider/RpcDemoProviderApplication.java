package com.lzh.rpc.demo.provider;

import com.lzh.rpc.core.model.server.ServerProperty;
import com.lzh.rpc.core.server.factory.BaseServerFactory;
import com.lzh.rpc.core.server.factory.RpcSpringServerFactory;
import com.lzh.rpc.starter.annotation.EnableRpcStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author Liuzihao
 */
@SpringBootApplication(scanBasePackages = {"com.lzh"})
public class RpcDemoProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDemoProviderApplication.class, args);
    }

    @Resource
    private ServerProperty serverProperty;

    @Bean
    public BaseServerFactory baseServerFactory() {
        return new RpcSpringServerFactory(serverProperty);
    }
}
