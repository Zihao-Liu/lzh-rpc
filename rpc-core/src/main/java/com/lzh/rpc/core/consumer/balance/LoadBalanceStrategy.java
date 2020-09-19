package com.lzh.rpc.core.consumer.balance;

/**
 * @author Liuzihao
 */
public interface LoadBalanceStrategy {
    /**
     * 获取请求地址
     *
     * @param providerName 服务提供者名称
     * @return 请求地址
     */
    String parseAddress(String providerName);
}
