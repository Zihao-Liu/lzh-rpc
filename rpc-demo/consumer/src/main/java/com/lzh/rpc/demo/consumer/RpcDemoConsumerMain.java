package com.lzh.rpc.demo.consumer;

import com.lzh.rpc.core.consumer.factory.RpcSimpleConsumerFactory;
import com.lzh.rpc.core.consumer.net.RpcConsumerLocalProxy;
import com.lzh.rpc.core.model.consumer.ConsumerProperty;
import com.lzh.rpc.demo.facade.TestFacade;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author Liuzihao
 */
public class RpcDemoConsumerMain {
    public static void main(String[] args) {
        ConsumerProperty consumerProperty = new ConsumerProperty();
        consumerProperty.setRegistry("test_service");
        consumerProperty.setService("test_service");
        consumerProperty.setDomain("localhost:8081");
        List<ConsumerProperty> properties = Lists.newArrayList();
        properties.add(consumerProperty);
        RpcSimpleConsumerFactory factory = new RpcSimpleConsumerFactory(properties);
        factory.start();
        TestFacade testFacade = RpcConsumerLocalProxy.invoke(TestFacade.class, consumerProperty);
        System.out.println(testFacade.testMethod1());
        factory.stop();
    }
}
