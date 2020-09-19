package com.lzh.rpc.common.model.provider;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务发现时返回的provider实例信息
 *
 * @author Liuzihao
 */
public class ProviderInstance implements Serializable {

    private String host;
    private Integer port;
    private Date lastLiveTime;

    public ProviderInstance() {
    }

    public ProviderInstance(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public ProviderInstance(String host, Integer port, Date lastLiveTime) {
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

    public Date getLastLiveTime() {
        return lastLiveTime;
    }

    public void setLastLiveTime(Date lastLiveTime) {
        this.lastLiveTime = lastLiveTime;
    }
}