package com.lzh.rpc.core.client.balance;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.MapUtils;

import com.lzh.rpc.common.model.client.ClientInvokeInfo;
import com.lzh.rpc.core.client.factory.BaseClientFactory;

/**
 * 随机调度算法: 会随机选取服务下任意一个可用的实例。也是默认的策略方式
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class RandomBalanceStrategy extends AbstractBalanceStrategy {

    private static final RandomBalanceStrategy INSTANCE = new RandomBalanceStrategy();

    private RandomBalanceStrategy() {
    }

    public static LoadBalanceStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<String> parseAddress(String providerName) {
        Map<String, ClientInvokeInfo> sourceMap = BaseClientFactory.getSourceMap(providerName);
        if (MapUtils.isEmpty(sourceMap)) {
            return Optional.empty();
        }
        int index = (int) (System.currentTimeMillis() % sourceMap.size());
        String address = sourceMap.keySet().toArray()[index].toString();
        return Optional.of(address);
    }
}