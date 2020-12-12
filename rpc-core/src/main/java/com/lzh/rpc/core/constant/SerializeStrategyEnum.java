package com.lzh.rpc.core.constant;

import com.lzh.rpc.core.serialize.strategy.HessianSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.KryoSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.ProtoStuffSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.SerializeStrategy;

/**
 * @author Liuzihao
 * @since 0.0.1
 */
public enum SerializeStrategyEnum {
    /**
     * 序列化方案
     */
    PROTO_STUFF("proto_stuff", ProtoStuffSerializeStrategy.class),

    HESSIAN("hessian", HessianSerializeStrategy.class),

    KRYO("kryo", KryoSerializeStrategy.class),
    ;

    private final String type;
    private final Class<? extends SerializeStrategy> serializeClass;

    SerializeStrategyEnum(String type, Class<? extends SerializeStrategy> serializeClass) {
        this.type = type;
        this.serializeClass = serializeClass;
    }


    public String getType() {
        return type;
    }

    public Class<? extends SerializeStrategy> getSerializeClass() {
        return serializeClass;
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
