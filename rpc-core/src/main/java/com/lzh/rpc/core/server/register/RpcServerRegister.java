package com.lzh.rpc.core.server.register;

import java.net.UnknownHostException;

import com.lzh.rpc.core.model.server.ServerProperty;

/**
 * RPC注册接口
 *
 * @author Liuzihao
 */
public interface RpcServerRegister {
    /**
     * 向注册中心注册当前实例
     *
     * @throws UnknownHostException UnknownHostException
     */
    void doRegister(ServerProperty property) throws UnknownHostException;

    /**
     * 向注册中心注销当前实例
     *
     * @throws UnknownHostException UnknownHostException
     */
    void doDestroy(ServerProperty property) throws UnknownHostException;
}
