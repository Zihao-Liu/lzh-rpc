package com.lzh.rpc.core.consumer.net;

import com.google.common.collect.Maps;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.consumer.ConsumerProperty;
import com.lzh.rpc.common.model.provider.ProviderInstance;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.common.util.HostUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Liuzihao
 */
public class RpcConsumerClientFactory {

    private RpcConsumerClientFactory() {
    }

    private static final Map<String, RpcConsumerNettyClient> CLIENT_MAP = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    static <T extends Serializable> RpcResponse<T> send(RpcRequest request, ConsumerProperty consumerProperty,
                                                        ProviderInstance providerInstance) throws RpcException {

        String key = consumerProperty.getRegistry() + "/" + HostUtil.getIpAndPort(providerInstance);
        RpcConsumerNettyClient client = CLIENT_MAP.get(key);
        if (client == null || !client.isValidate()) {
            client = new RpcConsumerNettyClient(consumerProperty, providerInstance);
            RpcConsumerNettyClient newClient = CLIENT_MAP.putIfAbsent(key, client);
            if (newClient != null && !client.equals(newClient)) {
                client.close();
                client = newClient;
            }
        }
        return client.send(request);
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
