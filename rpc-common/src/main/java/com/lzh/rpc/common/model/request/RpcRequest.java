package com.lzh.rpc.common.model.request;

import java.io.Serializable;
import java.util.Arrays;

import static com.lzh.rpc.common.constant.CommonConstant.HEART_BEAT_ID;


/**
 * @author Liuzihao
 */
public class RpcRequest implements Serializable {

    private String className;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] params;

    private RpcRequestHeader header;

    private RpcTraceInfo traceInfo;

    public static RpcRequest newBuilder() {
        return new RpcRequest();
    }

    public static RpcRequest hearBeat() {
        RpcTraceInfo traceInfo = RpcTraceInfo.newTrace(HEART_BEAT_ID);
        return newBuilder().setTraceInfo(traceInfo);
    }

    public String getClassName() {
        return className;
    }

    public RpcRequest setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public RpcRequest setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public RpcRequest setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public RpcRequest setParams(Object[] params) {
        this.params = params;
        return this;
    }

    public RpcRequestHeader getHeader() {
        return header;
    }

    public RpcRequest setHeader(RpcRequestHeader header) {
        this.header = header;
        return this;
    }

    public RpcTraceInfo getTraceInfo() {
        return traceInfo;
    }

    public RpcRequest setTraceInfo(RpcTraceInfo traceInfo) {
        this.traceInfo = traceInfo;
        return this;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", params=" + Arrays.toString(params) +
                ", header=" + header +
                '}';
    }
}
