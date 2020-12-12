package com.lzh.rpc.core.client.balance;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.MapUtils;

import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.client.ClientInvokeInfo;
import com.lzh.rpc.core.client.factory.BaseClientFactory;
import com.lzh.rpc.core.log.LoggerAdapter;

/**
 * 简单Ip哈希，通过调用方的Ip地址进行哈希的算法，不推荐使用
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class IpHashBalanceStrategy extends AbstractBalanceStrategy {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(IpHashBalanceStrategy.class);
    private static final IpHashBalanceStrategy INSTANCE = new IpHashBalanceStrategy();
    private static String localAddress;

    private IpHashBalanceStrategy() {
        try {
            localAddress = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            LOGGER.error("get local address error, ", e);
            throw RpcException.error(e);
        }
    }

    public static LoadBalanceStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<String> parseAddress(String providerName) {
        Map<String, ClientInvokeInfo> sourceMap = BaseClientFactory.getSourceMap(providerName);
        if (MapUtils.isEmpty(sourceMap)) {
            return Optional.empty();
        }
        int hash = localAddress.hashCode() % sourceMap.size();
        String address = sourceMap.keySet().toArray()[hash].toString();
        return Optional.of(address);
    }
}
