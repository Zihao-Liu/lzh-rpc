package com.lzh.rpc.demo.consumer.config;

import com.lzh.rpc.core.model.consumer.ConsumerProperty;;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "lzh.rpc")
public class ConsumerConfiguration {

    @NestedConfigurationProperty
    private List<ConsumerProperty> consumers;

    public List<ConsumerProperty> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<ConsumerProperty> consumers) {
        this.consumers = consumers;
    }
}
