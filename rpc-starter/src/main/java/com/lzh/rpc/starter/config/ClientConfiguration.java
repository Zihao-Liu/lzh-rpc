package com.lzh.rpc.starter.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import com.lzh.rpc.core.model.client.ClientProperty;

@Component
@ConfigurationProperties(prefix = "rpc-client")
public class ClientConfiguration {

    @NestedConfigurationProperty
    private List<ClientProperty> clients;

    public List<ClientProperty> getConsumers() {
        return clients;
    }

    public void setConsumers(List<ClientProperty> clients) {
        this.clients = clients;
    }
}
