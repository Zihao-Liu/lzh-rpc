package com.lzh.rpc.core.serialize.strategy;

import com.lzh.rpc.core.constant.SerializeStrategyEnum;

import java.util.Objects;

import static com.lzh.rpc.core.constant.SerializeStrategyEnum.HESSIAN;

/**
 * @author Liuzihao
 */
public abstract class AbstractSerializeStrategy implements SerializeStrategy {

    public static SerializeStrategy getStrategy(String type) {
        SerializeStrategyEnum strategy = SerializeStrategyEnum.typeOf(type);
        if (Objects.isNull(strategy)) {
            return HESSIAN.getSerializeStrategy();
        }
        return strategy.getSerializeStrategy();
    }
}
