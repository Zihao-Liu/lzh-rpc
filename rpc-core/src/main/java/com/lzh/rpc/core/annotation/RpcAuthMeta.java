package com.lzh.rpc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Liuzihao
 * Created on 2020-12-05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
@Documented
public @interface RpcAuthMeta {

    int appId();

    String token();
}
