package com.lzh.rpc.core.consumer.balance;


import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.core.constant.BalanceStrategyEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Liuzihao
 */
public abstract class AbstractBalanceStrategy implements LoadBalanceStrategy {

    public static LoadBalanceStrategy getStrategy(String strategyKey) {
        if (StringUtils.isBlank(strategyKey)) {
            return BalanceStrategyEnum.RANDOM.getBalanceStrategy();
        }
        BalanceStrategyEnum strategy = BalanceStrategyEnum.strategyOf(strategyKey);
        if (strategy == null) {
            throw RpcException.error("balance strategy not exist");
        }
        return strategy.getBalanceStrategy();
    }
}
