package com.lzh.rpc.core.model.provider;

import com.lzh.rpc.core.serialize.strategy.SerializeStrategy;
import com.sun.istack.internal.NotNull;

import java.util.Objects;

/**
 * RPC服务提供者所需要的基本信配置信息
 * TODO Lzh 增加注释信息和文档链接
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class ProviderProperty {

    /**
     * 是否启动Server
     */
    private Boolean enable;
    /**
     * 服务提供者使用的注册方式
     */
    private String register;
    /**
     * 注册中心地址
     */
    private String domain;
    /**
     * 服务注册心跳间隔(ms)
     */
    private Integer duration;
    /**
     * 服务Id，注册中心提供
     */
    private Integer appId;
    /**
     * 服务名称，注册中心提供
     */
    private String appName;
    /**
     * 服务Token，注册中心提供
     */
    private String appToken;
    /**
     * RPC服务端口
     */
    private Integer port;
    /**
     * 序列化方式
     */
    private String serialize;

    private Class<? extends SerializeStrategy> serializeClass;

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSerialize() {
        return serialize;
    }

    public void setSerialize(String serialize) {
        this.serialize = serialize;
    }

    @NotNull
    public Boolean getEnable() {
        return Objects.isNull(enable) || enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Class<? extends SerializeStrategy> getSerializeClass() {
        return serializeClass;
    }

    public void setSerializeClass(Class<? extends SerializeStrategy> serializeClass) {
        this.serializeClass = serializeClass;
    }
}
