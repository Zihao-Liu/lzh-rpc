package com.lzh.rpc.core.client.net;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Maps;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.common.util.HostUtil;
import com.lzh.rpc.core.annotation.RpcClient;
import com.lzh.rpc.core.model.client.ClientProperty;

/**
 * @author Liuzihao
 */
public class RpcClientNetFactory {

    private RpcClientNetFactory() {
    }

    private static final Map<String, RpcNettyClient> CLIENT_MAP = Maps.newConcurrentMap();

    static RpcResponse<? extends Serializable> send(RpcRequest request, ClientProperty clientProperty,
            ProviderInstance providerInstance, RpcClient rpcClient) throws RpcException {

        String key = clientProperty.getReference() + "/" + HostUtil.getIpAndPort(providerInstance);
        RpcNettyClient client = CLIENT_MAP.get(key);
        if (client == null || !client.isValidate()) {
            client = new RpcNettyClient(clientProperty, providerInstance);
            RpcNettyClient newClient = CLIENT_MAP.putIfAbsent(key, client);
            if (newClient != null && !client.equals(newClient)) {
                client.close();
                client = newClient;
            }
        }
        int timeout = 0;
        Integer clientTimeout = clientProperty.getTimeout();
        if (rpcClient.timeout() > 0 && Objects.nonNull(clientTimeout)) {
            timeout = Math.min(clientTimeout, rpcClient.timeout());
        } else if (rpcClient.timeout() > 0) {
            timeout = rpcClient.timeout();
        } else if (Objects.nonNull(clientTimeout)) {
            timeout = clientTimeout;
        }
        return client.send(request, timeout);
    }

    public static void close(String providerName, String address) {
        String key = providerName + "/" + address;
        CLIENT_MAP.computeIfPresent(key, (ignore, client) -> {
            client.close();
            return null;
        });
    }

    public static void clear() {
        synchronized (CLIENT_MAP) {
            CLIENT_MAP.forEach((key, client) -> client.close());
            CLIENT_MAP.clear();
        }
    }
}
