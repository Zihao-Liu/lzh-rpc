package com.lzh.rpc.core.consumer.balance;

import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.core.constant.BalanceStrategyEnum;
import com.lzh.rpc.core.model.consumer.ConsumerProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Liuzihao
 */
public abstract class AbstractBalanceStrategy implements LoadBalanceStrategy {

    public static LoadBalanceStrategy getStrategy(ConsumerProperty consumerProperty) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (consumerProperty.getBalanceClass() != null) {
            return consumerProperty.getBalanceClass().getConstructor().newInstance();
        }
        if (StringUtils.isBlank(consumerProperty.getBalance())) {
            return BalanceStrategyEnum.RANDOM.getBalanceStrategy();
        }
        BalanceStrategyEnum strategy = BalanceStrategyEnum.strategyOf(consumerProperty.getBalance());
        if (strategy == null) {
            throw RpcException.error("balance strategy not exist");
        }
        return strategy.getBalanceStrategy();
    }
}
