package com.lzh.rpc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

/**
 * RPC 服务提供者所需的注解，需要注解在提供服务的Service实现类上。
 * <p>
 * 如果使用Spring的方式进行启动，这个注解是必须的。如果不使用框架的方式启动，
 * 那么可以不使用该注解
 *
 * @author Liuzihao
 * @since 0.0.1
 */
@Service
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcServer {

    /**
     * Alias for {@link #name}.
     * 服务提供者的名称，如果不显式的指定这个名称，那么注入的时候会使用Service本身的名称进行注入
     */
    @AliasFor("name")
    String value() default "";

    /**
     * Alias for {@link #value}.
     * 服务提供者的名称，如果不显式的指定这个名称，那么注入的时候会使用Service本身的名称进行注入
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 服务提供者的实例，指定通过Rpc方式具体的实现了哪些接口
     */
    Class<?>[] types();

}
