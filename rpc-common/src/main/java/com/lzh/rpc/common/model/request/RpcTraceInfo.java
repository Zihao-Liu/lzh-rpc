package com.lzh.rpc.common.model.request;

import java.io.Serializable;

/**
 * @author Liuzihao
 */
public class RpcTraceInfo implements Serializable {
    /**
     * traceId，全局唯一
     */
    private String traceId;

    public static RpcTraceInfo newTrace(String traceId) {
        RpcTraceInfo rpcTraceInfo = new RpcTraceInfo();
        rpcTraceInfo.setTraceId(traceId);
        return rpcTraceInfo;
    }

    public String getTraceId() {
        return traceId;
    }

    public RpcTraceInfo setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
