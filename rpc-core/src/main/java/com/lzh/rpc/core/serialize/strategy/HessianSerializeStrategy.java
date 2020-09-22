package com.lzh.rpc.core.serialize.strategy;

import com.lzh.rpc.core.serialize.decoder.HessianDecoder;
import com.lzh.rpc.core.serialize.encoder.HessianEncoder;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Liuzihao
 */
public class HessianSerializeStrategy extends AbstractSerializeStrategy {

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
