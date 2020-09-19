package com.lzh.rpc.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC 服务提供者所需的注解，需要注解在提供服务的Service实现类上
 *
 * @author Liuzihao
 * @since 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcProvider {
    Class<?> value();
}
