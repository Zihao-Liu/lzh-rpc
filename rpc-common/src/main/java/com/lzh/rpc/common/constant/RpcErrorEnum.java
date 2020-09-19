package com.lzh.rpc.common.constant;

/**
 * @author Liuzihao
 */

public enum RpcErrorEnum {
    /**
     * 错误码
     */
    SUCCESS(0, "SUCCESS"),

    REQUEST_FILTER_ERROR(1, "REQUEST_FILTER_ERROR"),

    RESPONSE_FILTER_ERROR(2, "RESPONSE_FILTER_ERROR"),

    TIME_OUT(499, "TIME_OUT_ERROR"),

    NETTY_ERROR(504, "NETTY_ERROR"),

    PARAM_ERROR(505, "PARAM_ERROR"),

    PERMISSION_DENIED(403, "PERMISSION_DENIED"),

    ERROR(500, "SYSTEM_ERROR"),
    ;


    private Integer code;

    private String msg;

    RpcErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
