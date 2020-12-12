package com.lzh.rpc.core.processor;

import com.lzh.rpc.core.model.processor.ProcessorMeta;

/**
 * @author Liuzihao
 * Created on 2020-11-17
 */
public interface BaseProcessor {
    void doPreRequest(ProcessorMeta meta);

    void doPostRequest(ProcessorMeta meta);

    void doPreResponse(ProcessorMeta meta);

    void doPostResponse(ProcessorMeta meta);
}
