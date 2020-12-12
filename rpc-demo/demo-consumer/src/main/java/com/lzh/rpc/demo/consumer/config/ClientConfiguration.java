package com.lzh.rpc.demo.consumer.config;

import com.lzh.rpc.core.model.client.ClientProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Liuzihao
 */
@Component
@ConfigurationProperties(prefix = "rpc-client")
public class ClientConfiguration {

    @NestedConfigurationProperty
    private List<ClientProperty> clients;

    public List<ClientProperty> getClients() {
        return clients;
    }

    public void setClients(List<ClientProperty> clients) {
        this.clients = clients;
    }
}
