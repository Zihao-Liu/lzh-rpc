package com.lzh.rpc.common.model.consumer;


/**
 * Consumer基础配置
 *
 * @author Liuzihao
 */
public class ConsumerProperty {
    /**
     * 服务提供者标识，自定义，不可重复
     */
    private String registry;

    /**
     * 服务提供者名称
     */
    private String service;
    /**
     * 服务注册发现方式
     */
    private String register;
    /**
     * 服务注册中心域名，直连时为ip地址
     */
    private String domain;
    /**
     * 负载均衡方式
     */
    private String balance;

    /**
     * 请求用appId
     */
    private Integer appId;

    /**
     * 请求Provider时的token
     */
    private String appToken;


    /**
     * 请求注册中心时的token
     */
    private String accessToken;

    /**
     * 心跳间隔(ms)，默认为15000ms，取决于注册
     * 中心过期时间
     */
    private Integer duration;

    /**
     * 序列化方式
     */
    private String serialize;

    /**
     * netty超时时间
     */
    private Integer timeout;

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getSerialize() {
        return serialize;
    }

    public void setSerialize(String serialize) {
        this.serialize = serialize;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
