package com.lzh.rpc.starter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lzh.rpc.starter.config.ClientConfiguration;
import com.lzh.rpc.starter.config.ServerConfiguration;

/**
 * @author Liuzihao
 * Created on 2020-12-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ClientConfiguration.class, ServerConfiguration.class})
public @interface EnableRpcStarter {
    String value() default "";
}