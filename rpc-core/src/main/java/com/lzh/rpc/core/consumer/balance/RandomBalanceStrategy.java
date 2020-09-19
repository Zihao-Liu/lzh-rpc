package com.lzh.rpc.core.consumer.balance;

import com.lzh.rpc.common.model.consumer.ConsumerInvokeInfo;
import com.lzh.rpc.core.consumer.factory.BaseConsumerFactory;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author Liuzihao
 */
public class RandomBalanceStrategy extends AbstractBalanceStrategy {

    private static volatile RandomBalanceStrategy instance = null;

    public static RandomBalanceStrategy init() {
        if (Objects.isNull(instance)) {
            synchronized (RandomBalanceStrategy.class) {
                if (Objects.isNull(instance)) {
                    instance = new RandomBalanceStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    public String parseAddress(String providerName) {
        Map<String, ConsumerInvokeInfo> sourceMap = BaseConsumerFactory.getSourceMap(providerName);
        if (MapUtils.isEmpty(sourceMap)) {
            return null;
        }
        int index = (int) (System.currentTimeMillis() % sourceMap.size());
        return sourceMap.keySet().toArray(new String[0])[index];
    }
}