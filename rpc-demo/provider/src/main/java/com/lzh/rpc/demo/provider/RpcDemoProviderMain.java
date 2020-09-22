package com.lzh.rpc.demo.provider;

import  com.lzh.rpc.core.model.provider.ProviderProperty;
import com.lzh.rpc.core.provider.factory.RpcSimpleProviderFactory;
import com.lzh.rpc.demo.facade.TestFacade;

import java.net.UnknownHostException;

/**
 * @author Liuzihao
 */
public class RpcDemoProviderMain {
    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        ProviderProperty providerProperty = new ProviderProperty();
        providerProperty.setPort(8081);
        providerProperty.setEnable(true);
        RpcSimpleProviderFactory factory = new RpcSimpleProviderFactory(providerProperty)
                .addService(TestFacade.class, new TestProviderServiceImpl());
        factory.start();
        Thread.sleep(100000000000L);
        factory.stop();
    }
}
