package com.lzh.rpc.common.model.request;


import com.lzh.rpc.common.constant.RpcErrorEnum;
import com.lzh.rpc.common.exception.RpcException;

import java.io.Serializable;

import static com.lzh.rpc.common.constant.RpcErrorEnum.ERROR;
import static com.lzh.rpc.common.constant.RpcErrorEnum.SUCCESS;

/**
 * @author Liuzihao
 */
public class RpcResponse<T extends Serializable> implements Serializable {

    private RpcResponseHeader header;

    private Throwable error;

    private Integer code;

    private String msg;

    private T result;

    private RpcTraceInfo traceInfo;

    public RpcResponseHeader getHeader() {
        return header;
    }

    public RpcResponse<T> setHeader(RpcResponseHeader header) {
        this.header = header;
        return this;
    }

    public Throwable getError() {
        return error;
    }

    public RpcResponse<T> setError(Throwable error) {
        this.error = error;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public RpcResponse<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RpcResponse<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getResult() {
        return result;
    }

    public RpcResponse<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public RpcTraceInfo getTraceInfo() {
        return traceInfo;
    }

    public void setTraceInfo(RpcTraceInfo traceInfo) {
        this.traceInfo = traceInfo;
    }

    public static RpcResponse<RpcEmptyResult> error(RpcException exception) {
        return new RpcResponse<RpcEmptyResult>().setError(exception).setCode(exception.getCode()).setMsg(exception.getMsg());
    }


    public static RpcResponse<RpcEmptyResult> error(Exception exception) {
        return new RpcResponse<RpcEmptyResult>().setError(exception).setCode(ERROR.getCode()).setMsg(exception.getMessage());
    }

    public static RpcResponse<RpcEmptyResult> error(RpcErrorEnum errorNoEnum) {
        return new RpcResponse<RpcEmptyResult>().setError(RpcException.error(errorNoEnum))
                .setCode(errorNoEnum.getCode())
                .setMsg(errorNoEnum.getMsg());
    }

    public static <T extends Serializable> RpcResponse<T> success(T result) {
        return new RpcResponse<T>().setResult(result).setCode(SUCCESS.getCode());
    }


    @Override
    public String toString() {
        return "RpcResponse{" +
                "header=" + header +
                ", error=" + error +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
