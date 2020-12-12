package com.lzh.rpc.demo.provider;

import java.net.UnknownHostException;

import com.lzh.rpc.core.model.server.ServerProperty;
import com.lzh.rpc.core.server.factory.RpcSimpleServerFactory;
import com.lzh.rpc.demo.facade.TestFacade;

/**
 * @author Liuzihao
 */
public class RpcDemoProviderMain {
    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        ServerProperty providerProperty = new ServerProperty();
        providerProperty.setPort(8081);
        providerProperty.setEnable(true);
        providerProperty.setAppName("test-app2");
        providerProperty.setRegister("http");
        providerProperty.setDuration(5000);
        providerProperty.setDomain("http:localhost:10000");
        providerProperty.setAppToken("1234");
        providerProperty.setAppId(123);
        RpcSimpleServerFactory factory = new RpcSimpleServerFactory(providerProperty)
                .addService(TestFacade.class, new TestProviderServiceImpl());
        factory.start();
        Thread.sleep(100000000000L);
        factory.stop();
    }
}
