package com.lzh.rpc.core.constant;

import com.lzh.rpc.core.serialize.strategy.AbstractSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.HessianSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.KryoSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.ProtoStuffSerializeStrategy;

/**
 * @author Liuzihao
 */
public enum SerializeStrategyEnum {
    /**
     * 序列化方案
     */
    PROTO_STUFF("protostuff", ProtoStuffSerializeStrategy.init()),
    HESSIAN("hessian", HessianSerializeStrategy.init()),
    KRYO("kryo", KryoSerializeStrategy.init()),
    ;

    private String type;
    private AbstractSerializeStrategy serializeStrategy;

    SerializeStrategyEnum(String type, AbstractSerializeStrategy serializeStrategy) {
        this.type = type;
        this.serializeStrategy = serializeStrategy;
    }


    public String getType() {
        return type;
    }

    public AbstractSerializeStrategy getSerializeStrategy() {
        return serializeStrategy;
    }

    public static SerializeStrategyEnum typeOf(String type) {
        for (SerializeStrategyEnum typeEnum : SerializeStrategyEnum.values()) {
            if (typeEnum.type.equalsIgnoreCase(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}
