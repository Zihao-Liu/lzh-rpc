package com.lzh.rpc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

/**
 * @author Liuzihao
 * Created on 2020-12-05
 */
@Service
@Documented
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcAuthRule {
    boolean allowed() default true;

    RpcAuthMeta[] metas() default {};
}
