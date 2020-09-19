package com.lzh.rpc.core.serialize.strategy;

import com.lzh.rpc.core.serialize.decoder.HessianDecoder;
import com.lzh.rpc.core.serialize.encoder.HessianEncoder;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Objects;

/**
 * @author Liuzihao
 */
public class HessianSerializeStrategy extends AbstractSerializeStrategy {

    private static volatile HessianSerializeStrategy instance;

    public static HessianSerializeStrategy init() {
        if (Objects.isNull(instance)) {
            synchronized (HessianSerializeStrategy.class) {
                if (Objects.isNull(instance)) {
                    instance = new HessianSerializeStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> MessageToByteEncoder<T> getEncoder(Class<?> genericClass) {
        return new HessianEncoder(genericClass);
    }

    @Override
    public ByteToMessageDecoder getDecoder(Class<?> genericClass) {
        return new HessianDecoder(genericClass);
    }
}
