package com.lzh.rpc.core.serialize.strategy;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC序列化策略接口，RPC-CORE提供了多种序列化方式可供选择。默认使用
 * {@link HessianSerializeStrategy}策略。
 * <p>
 * 使用时的配置方式：{@link com.lzh.rpc.core.constant.SerializeStrategyEnum}
 * 1. Provider通过配置{@link com.lzh.rpc.common.model.provider.ProviderProperty}中的serialize字段进行配置。
 * 2. Consumer通过配置{@link com.lzh.rpc.common.model.consumer.ConsumerProperty}中的serialize字段进行配置。
 * <p>
 * 对于Consumer来说，调用不同的Provider可以使用不同的序列化策略。
 * <p>
 * 如果想要自己实现自定义策略模式，需要继承{@link AbstractSerializeStrategy}类，并且实现序列化和反序列化方法。
 * 然后在{@link com.lzh.rpc.core.constant.SerializeStrategyEnum}中增加对应的枚举和初始化方式。
 *
 * @author Liuzihao
 * @since 0.0.1
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
