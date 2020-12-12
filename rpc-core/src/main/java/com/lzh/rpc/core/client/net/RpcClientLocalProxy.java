package com.lzh.rpc.core.client.net;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.Optional;

import com.lzh.rpc.common.constant.CommonConstant;
import com.lzh.rpc.common.constant.RpcErrorEnum;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcRequestHeader;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.core.annotation.RpcClient;
import com.lzh.rpc.core.client.balance.AbstractBalanceStrategy;
import com.lzh.rpc.core.client.balance.LoadBalanceStrategy;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.client.ClientProperty;
import com.lzh.rpc.core.model.processor.ProcessorMeta;
import com.lzh.rpc.core.processor.ProcessorFactory;

/**
 * @author Liuzihao
 */
public class RpcClientLocalProxy {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(RpcClientLocalProxy.class);

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Class<?> interfaceClass, ClientProperty clientProperty, RpcClient rpcClient) {
        String providerName = clientProperty.getReference();
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass},
                (proxy, method, args) -> {
                    String className = method.getDeclaringClass().getName();
                    if (className.equals(Object.class.getName())) {
                        throw RpcException.error("class method not support, method: " + className);
                    }
                    ProviderInstance providerInstance = getRpcProviderInstance(clientProperty);
                    if (Objects.isNull(providerInstance)) {
                        throw RpcException.error(RpcErrorEnum.NO_PROVIDER_FOUND);
                    }

                    RpcRequestHeader header = new RpcRequestHeader();
                    header.setAppId(clientProperty.getAppId());
                    header.setTimeStamp(System.currentTimeMillis());

                    RpcRequest request = RpcRequest.newBuilder()
                            .setHeader(header)
                            .setClassName(method.getDeclaringClass().getName())
                            .setMethodName(method.getName())
                            .setParamTypes(method.getParameterTypes())
                            .setParams(args);

                    ProcessorMeta meta = new ProcessorMeta(clientProperty, request, providerInstance);
                    ProcessorFactory.doPreRequest(meta);

                    LOGGER.info("send rpc request begin, provider: {}, instance: {}, request: {}", providerName,
                            providerInstance, request);
                    RpcResponse<? extends Serializable> response = null;
                    try {
                        response = RpcClientNetFactory.send(request, clientProperty, providerInstance, rpcClient);
                    } catch (RpcException e) {
                        LOGGER.error("rpc invoke error, caused by: {}", e.getMessage());
                        response = RpcResponse.error(RpcErrorEnum.NO_RESPONSE);
                        throw e;
                    } finally {
                        meta.setResponse(response);
                        ProcessorFactory.doPostRequest(meta);
                    }
                    if (RpcErrorEnum.SUCCESS.getCode().equals(response.getCode())) {
                        return response.getResult();
                    }
                    throw response.getError();
                });
    }

    private static ProviderInstance getRpcProviderInstance(ClientProperty clientProperty) {
        LoadBalanceStrategy strategy = AbstractBalanceStrategy.getStrategy(clientProperty);

        Optional<String> address = strategy.parseAddress(clientProperty.getReference());
        if (!address.isPresent()) {
            return null;
        }
        String[] hostAndPort = address.get().split(CommonConstant.IP_AND_PORT_SPLIT);
        return new ProviderInstance(hostAndPort[0], Integer.valueOf(hostAndPort[1]));
    }
}
