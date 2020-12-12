package com.lzh.rpc.core.client.factory;

import java.util.List;

import com.lzh.rpc.core.model.client.ClientProperty;

/**
 * @author Liuzihao
 */
public class RpcSimpleClientFactory extends BaseClientFactory {

    private final List<ClientProperty> clientPropertyList;

    public RpcSimpleClientFactory(List<ClientProperty> clientPropertyList) {
        this.clientPropertyList = clientPropertyList;
    }

    @Override
    List<ClientProperty> listClientProperty() {
        return clientPropertyList;
    }
}
