package com.lzh.rpc.core.constant;

import com.lzh.rpc.core.client.balance.IpHashBalanceStrategy;
import com.lzh.rpc.core.client.balance.LoadBalanceStrategy;
import com.lzh.rpc.core.client.balance.RandomBalanceStrategy;
import com.lzh.rpc.core.client.balance.RoundRobinBalanceStrategy;

/**
 * 负载均衡算法，默认支持3种策略方式
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public enum BalanceStrategyEnum {
    /**
     * 随机调度算法
     */
    RANDOM("random", RandomBalanceStrategy.getInstance()),
    /**
     * 轮询调度算法
     */
    ROUND_ROBIN("round", RoundRobinBalanceStrategy.getInstance()),
    /**
     * 简单Ip哈希算法
     */
    IP_HASH("ip", IpHashBalanceStrategy.getInstance()),
    ;
    private final String strategy;
    private final LoadBalanceStrategy instance;

    BalanceStrategyEnum(String strategy, LoadBalanceStrategy instance) {
        this.strategy = strategy;
        this.instance = instance;
    }

    public String getStrategy() {
        return strategy;
    }

    public LoadBalanceStrategy getInstance() {
        return instance;
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
