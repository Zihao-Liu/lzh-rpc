package com.lzh.rpc.core.serialize.strategy;

import com.lzh.rpc.core.serialize.decoder.KryoDecoder;
import com.lzh.rpc.core.serialize.encoder.KryoEncoder;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Objects;

/**
 * @author Liuzihao
 */
public class KryoSerializeStrategy extends AbstractSerializeStrategy {

    private static volatile KryoSerializeStrategy instance;

    public static KryoSerializeStrategy init() {
        if (Objects.isNull(instance)) {
            synchronized (KryoSerializeStrategy.class) {
                if (Objects.isNull(instance)) {
                    instance = new KryoSerializeStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> MessageToByteEncoder<T> getEncoder(Class<?> genericClass) {
        return new KryoEncoder(genericClass);
    }

    @Override
    public ByteToMessageDecoder getDecoder(Class<?> genericClass) {
        return new KryoDecoder(genericClass);
    }
}

