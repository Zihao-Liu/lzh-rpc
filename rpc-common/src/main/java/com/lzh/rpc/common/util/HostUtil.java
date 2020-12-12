package com.lzh.rpc.common.util;

import com.lzh.rpc.common.model.server.ProviderInstance;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.lzh.rpc.common.constant.CommonConstant.ADDRESS_FORMAT;

/**
 * @author Liuzihao
 */
public class HostUtil {

    private HostUtil() {
    }

    public static String getHostAddress() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostAddress();
    }

    public static String getIpAndPort(ProviderInstance providerInstance) {
        return String.format(ADDRESS_FORMAT, providerInstance.getHost(), providerInstance.getPort());
    }

    public static String formatAddress(String host, Integer port) {
        return String.format(ADDRESS_FORMAT, host, port);
    }
}
