package com.lzh.rpc.core.consumer.discover;

import com.lzh.rpc.common.model.consumer.ConsumerProperty;

import java.util.List;

/**
 * @author Liuzihao
 */
public interface RpcConsumerDiscover {
    /**
     * 停止服务发现
     */
    void doDestroy();

    void doDiscover(List<ConsumerProperty> sources);
}
