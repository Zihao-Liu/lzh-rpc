package com.lzh.rpc.core.model.server;

import java.util.List;
import java.util.Objects;

import com.lzh.rpc.core.processor.BaseProcessor;
import com.lzh.rpc.core.serialize.strategy.SerializeStrategy;

/**
 * RPC服务提供者所需要的基本信配置信息
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class ServerProperty {

    /**
     * 是否启动Server
     */
    private Boolean enable;
    /**
     * 服务提供者使用的注册方式，{@link com.lzh.rpc.core.constant.RegisterTypeEnum}.type
     */
    private String register;
    /**
     * 注册中心地址，注册方式为DIRECT时，可不填
     */
    private String domain;
    /**
     * 服务注册心跳间隔(ms)，默认为15000ms，取决于注册
     * 中心过期时间
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

    private Boolean enableAuth;

    private List<Class<? extends BaseProcessor>> processors;

    private Integer threadNum;

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

    public Boolean getEnableAuth() {
        return Objects.nonNull(enableAuth) && enableAuth;
    }

    public void setEnableAuth(Boolean enableAuth) {
        this.enableAuth = enableAuth;
    }

    public List<Class<? extends BaseProcessor>> getProcessors() {
        return processors;
    }

    public void setProcessors(List<Class<? extends BaseProcessor>> processors) {
        this.processors = processors;
    }

    public Integer getThreadNum() {
        return Objects.isNull(threadNum) ? 100 : threadNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }
}
