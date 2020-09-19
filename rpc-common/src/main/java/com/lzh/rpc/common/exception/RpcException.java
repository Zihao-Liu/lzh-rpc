package com.lzh.rpc.common.exception;


import com.lzh.rpc.common.constant.RpcErrorEnum;

/**
 * @author Liuzihao
 */
public class RpcException extends RuntimeException {

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RpcException(RpcErrorEnum errorNoEnum) {
        super(errorNoEnum.getMsg());
        this.code = errorNoEnum.getCode();
        this.msg = errorNoEnum.getMsg();
    }

    public RpcException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public static RpcException error(String msg) {
        return new RpcException(RpcErrorEnum.ERROR.getCode(), msg);
    }

    public static RpcException error() {
        return new RpcException(RpcErrorEnum.ERROR);
    }

    public static RpcException error(RpcErrorEnum errorNoEnum) {
        return new RpcException(errorNoEnum);
    }

    public static RpcException error(Throwable t) {
        return RpcException.error(t.getMessage());
    }
}
