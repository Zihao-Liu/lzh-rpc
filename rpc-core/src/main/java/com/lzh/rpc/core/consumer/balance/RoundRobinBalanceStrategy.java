package com.lzh.rpc.core.consumer.balance;

import com.lzh.rpc.common.model.consumer.ConsumerInvokeInfo;
import com.lzh.rpc.core.consumer.factory.BaseConsumerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @author Liuzihao
 */
public class RoundRobinBalanceStrategy extends AbstractBalanceStrategy {

    private static volatile RoundRobinBalanceStrategy instance = null;

    public static RoundRobinBalanceStrategy init() {
        if (Objects.isNull(instance)) {
            synchronized (RoundRobinBalanceStrategy.class) {
                if (Objects.isNull(instance)) {
                    instance = new RoundRobinBalanceStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    public String parseAddress(String providerName) {
        Map<String, ConsumerInvokeInfo> sourceMap = BaseConsumerFactory.getSourceMap(providerName);
        return sourceMap.entrySet().stream().findFirst().map(Map.Entry::getKey).orElse(null);
    }
}
