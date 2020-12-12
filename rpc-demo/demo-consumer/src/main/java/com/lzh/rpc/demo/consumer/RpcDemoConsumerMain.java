package com.lzh.rpc.demo.consumer;

import com.lzh.rpc.core.client.factory.RpcSimpleClientFactory;
import com.lzh.rpc.core.client.net.RpcClientLocalProxy;
import com.lzh.rpc.core.model.client.ClientProperty;
import com.lzh.rpc.demo.facade.TestFacade;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author Liuzihao
 */
public class RpcDemoConsumerMain {
    public static void main(String[] args) {
        ClientProperty clientProperty = new ClientProperty();
        clientProperty.setReference("test_service");
        clientProperty.setService("test_service");
        clientProperty.setDomain("localhost:8081");
        List<ClientProperty> properties = Lists.newArrayList();
        properties.add(clientProperty);
        RpcSimpleClientFactory factory = new RpcSimpleClientFactory(properties);
        factory.start();
        TestFacade testFacade = RpcClientLocalProxy.invoke(TestFacade.class, clientProperty, null);
        System.out.println(testFacade.testMethod1());
        factory.stop();
    }
}
