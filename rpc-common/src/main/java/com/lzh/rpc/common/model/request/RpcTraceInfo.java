package com.lzh.rpc.common.model.request;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Liuzihao
 */
public class RpcTraceInfo implements Serializable {
    /**
     * traceId，全局唯一
     */
    private String traceId;
    /**
     * 调用链层级
     */
    private Integer level;
    /**
     * 同一层级的次序
     */
    private Integer spanId;
    /**
     * 请求时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;

    public static RpcTraceInfo newTrace(String traceId) {
        return newTrace(traceId, System.currentTimeMillis());
    }

    public static RpcTraceInfo newTrace(String traceId, Long timestamp) {
        RpcTraceInfo rpcTraceInfo = new RpcTraceInfo();
        return rpcTraceInfo.setTraceId(traceId)
                .setLevel(1)
                .setSpanId(1)
                .setStartTime(timestamp);
    }

    public static RpcTraceInfo nextSpan(RpcTraceInfo traceInfo) {
        RpcTraceInfo rpcTraceInfo = new RpcTraceInfo();
        return rpcTraceInfo
                .setTraceId(traceInfo.getTraceId())
                .setLevel(traceInfo.getLevel())
                .setSpanId(traceInfo.getSpanId() + 1)
                .setStartTime(System.currentTimeMillis());
    }

    public static RpcTraceInfo nextLevel(RpcTraceInfo traceInfo) {
        RpcTraceInfo rpcTraceInfo = new RpcTraceInfo();
        return rpcTraceInfo
                .setTraceId(traceInfo.getTraceId())
                .setLevel(traceInfo.getLevel() + 1)
                .setSpanId(1)
                .setStartTime(System.currentTimeMillis());
    }

    public String getTraceId() {
        return traceId;
    }

    public RpcTraceInfo setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public RpcTraceInfo setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public RpcTraceInfo setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public Integer getSpanId() {
        return spanId;
    }

    public RpcTraceInfo setSpanId(Integer spanId) {
        this.spanId = spanId;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public RpcTraceInfo setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public String toString() {
        return "RpcTraceInfo{" +
                "traceId='" + traceId + '\'' +
                ", level=" + level +
                ", spanId=" + spanId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public String toHashString() {
        return "RpcTraceInfo{" +
                "traceId='" + traceId + '\'' +
                ", level=" + level +
                ", spanId=" + spanId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RpcTraceInfo traceInfo = (RpcTraceInfo) o;
        return traceId.equals(traceInfo.traceId) &&
                level.equals(traceInfo.level) &&
                spanId.equals(traceInfo.spanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traceId, level, spanId);
    }
}
