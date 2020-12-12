package com.lzh.rpc.core.model.processor;

import java.io.Serializable;

import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.core.model.client.ClientProperty;
import com.lzh.rpc.core.model.server.ServerProperty;

/**
 * @author Liuzihao
 * Created on 2020-11-17
 */
public class ProcessorMeta {
    private ClientProperty clientProperty;
    private ServerProperty providerProperty;
    private RpcRequest request;
    private RpcResponse<? extends Serializable> response;
    private ProviderInstance instance;

    public ProcessorMeta() {
    }

    public ProcessorMeta(ClientProperty property, RpcRequest request, ProviderInstance instance) {
        this.clientProperty = property;
        this.request = request;
        this.instance = instance;
    }

    public ProcessorMeta(ServerProperty property, RpcRequest request, RpcResponse<? extends Serializable> response) {
        this.providerProperty = property;
        this.request = request;
        this.response = response;
    }

    public ClientProperty getClientProperty() {
        return clientProperty;
    }

    public void setClientProperty(ClientProperty clientProperty) {
        this.clientProperty = clientProperty;
    }

    public ServerProperty getServerProperty() {
        return providerProperty;
    }

    public void setServerProperty(ServerProperty providerProperty) {
        this.providerProperty = providerProperty;
    }

    public RpcRequest getRequest() {
        return request;
    }

    public void setRequest(RpcRequest request) {
        this.request = request;
    }

    public RpcResponse<? extends Serializable> getResponse() {
        return response;
    }

    public void setResponse(RpcResponse<? extends Serializable> response) {
        this.response = response;
    }

    public ProviderInstance getInstance() {
        return instance;
    }

    public void setInstance(ProviderInstance instance) {
        this.instance = instance;
    }
}
