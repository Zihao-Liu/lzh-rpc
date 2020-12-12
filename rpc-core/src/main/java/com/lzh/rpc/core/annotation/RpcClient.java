package com.lzh.rpc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rpc client端使用的注解，通过这个注解来标识service调用时，使用rpc的方式进行代理
 *
 * @author Liuzihao
 * @since 0.0.1
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcClient {
    /**
     * 表示使用哪个rpc配置进行代理，应该与{@link com.lzh.rpc.core.model.client.ClientProperty}
     * 中配置的reference字段相匹配
     */
    String reference();

    /**
     * 调用超时时间，TimeUnit 毫秒(ms)
     */
    int timeout() default 0;
}
