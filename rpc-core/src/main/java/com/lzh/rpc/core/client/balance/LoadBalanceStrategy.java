package com.lzh.rpc.core.client.balance;

import java.util.Optional;

/**
 * @author Liuzihao
 * @since 0.0.1
 */
public interface LoadBalanceStrategy {
    /**
     * 获取请求地址
     *
     * @param providerName 服务提供者名称
     * @return 请求地址
     */
    Optional<String> parseAddress(String providerName);
}
