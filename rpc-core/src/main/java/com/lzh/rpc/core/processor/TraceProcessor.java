package com.lzh.rpc.core.processor;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.BeanUtils;

import com.lzh.rpc.common.model.request.RpcTraceInfo;
import com.lzh.rpc.common.util.JsonUtil;
import com.lzh.rpc.core.constant.LocalContext;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.processor.ProcessorMeta;

/**
 * @author Liuzihao
 * Created on 2020-11-17
 */
public class TraceProcessor implements BaseProcessor {
    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(TraceProcessor.class);

    private static final BlockingQueue<ProcessorMeta> TRACE_QUEUE = new LinkedBlockingDeque<>();

    @Override
    public void doPreRequest(ProcessorMeta meta) {
        RpcTraceInfo traceInfo = LocalContext.getTrace();
        if (Objects.isNull(traceInfo)) {
            Long timestamp = meta.getRequest().getHeader().getTimeStamp();
            // ThreadLocal里没有Trace信息，说明是第一次调用，初始化
            traceInfo = RpcTraceInfo.newTrace(UUID.randomUUID().toString(), timestamp);
        }
        meta.getRequest().setTraceInfo(traceInfo);
        // 下一次请求的TraceId
        LocalContext.setTrace(RpcTraceInfo.nextSpan(traceInfo));
    }

    @Override
    public void doPostRequest(ProcessorMeta meta) {
        RpcTraceInfo traceInfo = meta.getRequest().getTraceInfo();
        traceInfo.setEndTime(System.currentTimeMillis());
        meta.getRequest().setTraceInfo(traceInfo);
        if (!TRACE_QUEUE.add(meta)) {
            LOGGER.debug("trace queue is blocked, drop info: {}", JsonUtil.toJson(meta));
        }
    }

    @Override
    public void doPreResponse(ProcessorMeta meta) {
        RpcTraceInfo requestInfo = meta.getRequest().getTraceInfo();
        RpcTraceInfo responseInfo = new RpcTraceInfo();
        BeanUtils.copyProperties(requestInfo, responseInfo);
        responseInfo.setStartTime(System.currentTimeMillis());
        meta.getResponse().setTraceInfo(responseInfo);

        LocalContext.setTrace(RpcTraceInfo.nextLevel(requestInfo));
    }

    @Override
    public void doPostResponse(ProcessorMeta meta) {
        RpcTraceInfo responseInfo = meta.getResponse().getTraceInfo();
        responseInfo.setEndTime(System.currentTimeMillis());

        meta.getResponse().setTraceInfo(responseInfo);

        if (!TRACE_QUEUE.add(meta)) {
            LOGGER.debug("trace queue is blocked, drop info: {}", JsonUtil.toJson(meta));
        }
        LocalContext.clear();
    }
}
