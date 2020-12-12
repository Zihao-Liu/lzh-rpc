package com.lzh.rpc.core.client.discover;

import java.util.List;

import com.lzh.rpc.core.model.client.ClientProperty;

/**
 * @author Liuzihao
 */
public interface RpcClientDiscover {
    /**
     * 停止服务发现
     */
    void doDestroy();

    void doDiscover(List<ClientProperty> consumerPropertyList);
}
