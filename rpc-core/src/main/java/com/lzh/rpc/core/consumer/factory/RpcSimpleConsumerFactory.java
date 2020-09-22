package com.lzh.rpc.core.consumer.factory;

import com.lzh.rpc.core.model.consumer.ConsumerProperty;

import java.util.List;

/**
 * @author Liuzihao
 */
public class RpcSimpleConsumerFactory extends BaseConsumerFactory {

    private List<ConsumerProperty> consumerPropertyList;

    public RpcSimpleConsumerFactory(List<ConsumerProperty> consumerPropertyList) {
        this.consumerPropertyList = consumerPropertyList;
    }

    @Override
    List<ConsumerProperty> listConsumerProperty() {
        return consumerPropertyList;
    }
}
