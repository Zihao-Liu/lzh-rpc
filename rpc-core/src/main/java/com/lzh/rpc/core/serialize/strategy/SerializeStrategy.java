package com.lzh.rpc.core.serialize.strategy;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Liuzihao
 */
public interface SerializeStrategy {
    /**
     * 获取Encoder实例
     *
     * @param genericClass 需要Encode的对象类型
     * @return 策略对应的Encoder实例
     */
    <T> MessageToByteEncoder<T> getEncoder(Class<?> genericClass);

    /**
     * 获取Decoder实例
     *
     * @param genericClass 需要Decode的对象类型
     * @return 策略对应的Decoder实例
     */
    ByteToMessageDecoder getDecoder(Class<?> genericClass);
}
