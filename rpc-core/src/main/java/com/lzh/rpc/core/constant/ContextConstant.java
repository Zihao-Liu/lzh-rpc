package com.lzh.rpc.core.constant;

import okhttp3.MediaType;

/**
 * @author Liuzihao
 * @since 0.0.1
 */
public class ContextConstant {
    private ContextConstant() {
    }

    /**
     * 注册接口后缀
     */
    public static final String REGISTER_URL_SUFFIX = "/register";
    /**
     * 注销接口后缀
     */
    public static final String DESTROY_URL_SUFFIX = "/destroy";

    public static final Integer REGISTER_TIMEOUT_MILLS = 5000;

    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
}
