package com.lzh.rpc.core.consumer.net;

import com.lzh.rpc.common.constant.CommonConstant;
import com.lzh.rpc.common.constant.RpcErrorEnum;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.provider.ProviderInstance;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcRequestHeader;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.common.model.request.RpcTraceInfo;
import com.lzh.rpc.core.consumer.balance.AbstractBalanceStrategy;
import com.lzh.rpc.core.consumer.balance.LoadBalanceStrategy;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.consumer.ConsumerProperty;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author Liuzihao
 */
public class RpcConsumerLocalProxy {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(RpcConsumerLocalProxy.class);

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Class<?> interfaceClass, ConsumerProperty consumerProperty) {
        String providerName = consumerProperty.getRegistry();
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    String className = method.getDeclaringClass().getName();
                    if (className.equals(Object.class.getName())) {
                        throw RpcException.error("class method not support, method: " + className);
                    }
                    RpcRequestHeader header = new RpcRequestHeader();
                    RpcTraceInfo traceInfo = RpcTraceInfo.newTrace(UUID.randomUUID().toString());
                    RpcRequest request = RpcRequest.newBuilder()
                            .setHeader(header)
                            .setClassName(method.getDeclaringClass().getName())
                            .setMethodName(method.getName())
                            .setParamTypes(method.getParameterTypes())
                            .setTraceInfo(traceInfo)
                            .setParams(args);

                    ProviderInstance providerInstance = getRpcProviderInstance(consumerProperty);
                    LOGGER.info("send rpc request begin, provider: {}, instance: {}, request: {}", providerName, providerInstance, request);
                    RpcResponse<Serializable> response;
                    try {
                        response = RpcConsumerClientFactory.send(request, consumerProperty, providerInstance);
                    } catch (RpcException e) {
                        LOGGER.error("rpc invoke error, caused by: {}", e.getMessage());
                        throw e;
                    }
                    if (response == null) {
                        throw RpcException.error();
                    }
                    if (RpcErrorEnum.SUCCESS.getCode().equals(response.getCode())) {
                        return response.getResult();
                    }
                    throw response.getError();
                });
    }

    private static ProviderInstance getRpcProviderInstance(ConsumerProperty consumerProperty) throws Exception {
        LoadBalanceStrategy strategy = AbstractBalanceStrategy.getStrategy(consumerProperty);
        String address = strategy.parseAddress(consumerProperty.getRegistry());
        String[] hostAndPort = address.split(CommonConstant.IP_AND_PORT_SPLIT);
        ProviderInstance providerInstance = new ProviderInstance();
        providerInstance.setHost(hostAndPort[0]);
        providerInstance.setPort(Integer.valueOf(hostAndPort[1]));
        return providerInstance;
    }
}
