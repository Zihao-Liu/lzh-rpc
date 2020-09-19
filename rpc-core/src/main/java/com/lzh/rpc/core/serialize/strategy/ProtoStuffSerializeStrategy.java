package com.lzh.rpc.core.serialize.strategy;

import com.lzh.rpc.core.serialize.decoder.ProtoStuffDecoder;
import com.lzh.rpc.core.serialize.encoder.ProtoStuffEncoder;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Objects;

/**
 * @author Liuzihao
 */
public class ProtoStuffSerializeStrategy extends AbstractSerializeStrategy {

    private static volatile ProtoStuffSerializeStrategy instance;

    public static ProtoStuffSerializeStrategy init() {
        if (Objects.isNull(instance)) {
            synchronized (ProtoStuffSerializeStrategy.class) {
                if (Objects.isNull(instance)) {
                    instance = new ProtoStuffSerializeStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> MessageToByteEncoder<T> getEncoder(Class<?> genericClass) {
        return new ProtoStuffEncoder(genericClass);
    }

    @Override
    public ByteToMessageDecoder getDecoder(Class<?> genericClass) {
        return new ProtoStuffDecoder(genericClass);
    }
}
