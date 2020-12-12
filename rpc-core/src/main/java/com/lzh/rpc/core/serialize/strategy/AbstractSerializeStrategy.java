package com.lzh.rpc.core.serialize.strategy;

import com.lzh.rpc.core.constant.SerializeStrategyEnum;
import com.lzh.rpc.core.model.client.ClientProperty;
import com.lzh.rpc.core.model.server.ServerProperty;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static com.lzh.rpc.core.constant.SerializeStrategyEnum.HESSIAN;

/**
 * @author Liuzihao
 */
public abstract class AbstractSerializeStrategy implements SerializeStrategy {

    public static SerializeStrategy getStrategy(ServerProperty property) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends SerializeStrategy> clazz = property.getSerializeClass();
        if (clazz != null) {
            return clazz.getConstructor().newInstance();
        }
        SerializeStrategyEnum strategy = SerializeStrategyEnum.typeOf(property.getSerialize());
        if (Objects.isNull(strategy)) {
            strategy = HESSIAN;
        }
        return strategy.getSerializeClass().getConstructor().newInstance();
    }

    public static SerializeStrategy getStrategy(ClientProperty property) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends SerializeStrategy> clazz = property.getSerializeClass();
        if (clazz != null) {
            return clazz.getConstructor().newInstance();
        }
        SerializeStrategyEnum strategy = SerializeStrategyEnum.typeOf(property.getSerialize());
        if (Objects.isNull(strategy)) {
            strategy = HESSIAN;
        }
        return strategy.getSerializeClass().getConstructor().newInstance();
    }
}
