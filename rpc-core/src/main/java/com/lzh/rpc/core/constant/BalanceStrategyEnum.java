package com.lzh.rpc.core.constant;

import com.lzh.rpc.core.consumer.balance.AbstractBalanceStrategy;
import com.lzh.rpc.core.consumer.balance.IpHashBalanceStrategy;
import com.lzh.rpc.core.consumer.balance.RandomBalanceStrategy;
import com.lzh.rpc.core.consumer.balance.RoundRobinBalanceStrategy;

/**
 * @author Liuzihao
 */
public enum BalanceStrategyEnum {
    /**
     * 负载均衡方式
     */
    RANDOM("random", RandomBalanceStrategy.init()),
    ROUND_ROBIN("round", RoundRobinBalanceStrategy.init()),
    IP_HASH("ip", IpHashBalanceStrategy.init()),
    ;
    private String strategy;
    private AbstractBalanceStrategy balanceStrategy;

    BalanceStrategyEnum(String strategy, AbstractBalanceStrategy balanceStrategy) {
        this.strategy = strategy;
        this.balanceStrategy = balanceStrategy;
    }

    public String getStrategy() {
        return strategy;
    }

    public AbstractBalanceStrategy getBalanceStrategy() {
        return balanceStrategy;
    }

    public static BalanceStrategyEnum strategyOf(String strategy) {
        for (BalanceStrategyEnum strategyEnum : BalanceStrategyEnum.values()) {
            if (strategyEnum.strategy.equalsIgnoreCase(strategy)) {
                return strategyEnum;
            }
        }
        return null;
    }
}
