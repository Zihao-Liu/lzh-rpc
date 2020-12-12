package com.lzh.rpc.core.constant;

import com.lzh.rpc.common.model.request.RpcTraceInfo;

/**
 * @author Liuzihao
 * @since 0.0.1
 */
public class LocalContext {
    private LocalContext() {
    }

    private static final ThreadLocal<RpcTraceInfo> LOCAL_TRACE = ThreadLocal.withInitial(() -> null);

    public static void setTrace(RpcTraceInfo traceInfo) {
        LOCAL_TRACE.set(traceInfo);
    }

    public static RpcTraceInfo getTrace() {
        return LOCAL_TRACE.get();
    }

    public static void clear() {
        LOCAL_TRACE.remove();
    }
}
