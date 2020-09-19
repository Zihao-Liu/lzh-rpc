package com.lzh.rpc.core.consumer.balance;

import com.lzh.rpc.common.model.consumer.ConsumerInvokeInfo;
import com.lzh.rpc.core.consumer.factory.BaseConsumerFactory;
import com.lzh.rpc.core.log.LoggerAdapter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * @author Liuzihao
 */
public class IpHashBalanceStrategy extends AbstractBalanceStrategy {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(IpHashBalanceStrategy.class);
    private static volatile IpHashBalanceStrategy instance = null;

    private static String localAddress;

    static {
        try {
            localAddress = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            LOGGER.error("get local address error, ", e);
        }
    }

    public static IpHashBalanceStrategy init() {
        if (Objects.isNull(instance)) {
            synchronized (IpHashBalanceStrategy.class) {
                if (Objects.isNull(instance)) {
                    instance = new IpHashBalanceStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    public String parseAddress(String providerName) {
        Map<String, ConsumerInvokeInfo> sourceMap = BaseConsumerFactory.getSourceMap(providerName);
        int hash = localAddress.hashCode() % sourceMap.size();
        return new ArrayList<>(sourceMap.keySet()).get(hash);
    }
}
