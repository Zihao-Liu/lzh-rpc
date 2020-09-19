package com.lzh.rpc.core.provider.register;

import java.net.UnknownHostException;

/**
 * RPC注册接口
 *
 * @author Liuzihao
 */
public interface RpcProviderRegister {
    /**
     * 向注册中心注册当前实例
     *
     * @throws UnknownHostException UnknownHostException
     */
    void doRegister() throws UnknownHostException;

    /**
     * 向注册中心注销当前实例
     *
     * @throws UnknownHostException UnknownHostException
     */
    void doDestroy() throws UnknownHostException;
}
