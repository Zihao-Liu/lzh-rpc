package com.lzh.rpc.core.client.balance;

import java.util.Map;
import java.util.Optional;

import com.lzh.rpc.common.model.client.ClientInvokeInfo;
import com.lzh.rpc.core.client.factory.BaseClientFactory;

/**
 * 轮询调度算法，并不是严格轮询，实现方式依赖LinkedHashMap维护的顺序关系，
 * 实例的增加/删除也可能导致调度顺序改变
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class RoundRobinBalanceStrategy extends AbstractBalanceStrategy {

    private static final RoundRobinBalanceStrategy INSTANCE = new RoundRobinBalanceStrategy();

    public static LoadBalanceStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<String> parseAddress(String providerName) {
        Map<String, ClientInvokeInfo> sourceMap = BaseClientFactory.getSourceMap(providerName);
        return sourceMap.entrySet().stream().findFirst().map(Map.Entry::getKey);
    }
}
