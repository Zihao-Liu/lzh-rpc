package com.lzh.rpc.common.model.server;

import java.io.Serializable;

import com.lzh.rpc.common.constant.CommonConstant;

/**
 * 服务发现时返回的provider实例信息
 *
 * @author Liuzihao
 */
public class ProviderInstance implements Serializable {

    private String host;
    private Integer port;
    private Long lastLiveTime;

    public ProviderInstance() {
    }

    public ProviderInstance(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public ProviderInstance(String address, Long lastLiveTime) {
        String[] hostAndPort = address.split(CommonConstant.IP_AND_PORT_SPLIT);
        this.host = hostAndPort[0];
        this.port = Integer.valueOf(hostAndPort[1]);
        this.lastLiveTime = lastLiveTime;
    }

    public ProviderInstance(String address) {
        this(address, System.currentTimeMillis());
    }

    public ProviderInstance(String host, Integer port, Long lastLiveTime) {
        this.host = host;
        this.port = port;
        this.lastLiveTime = lastLiveTime;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Long getLastLiveTime() {
        return lastLiveTime;
    }

    public void setLastLiveTime(Long lastLiveTime) {
        this.lastLiveTime = lastLiveTime;
    }

    @Override
    public String toString() {
        return "ProviderInstance{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", lastLiveTime=" + lastLiveTime +
                '}';
    }
}