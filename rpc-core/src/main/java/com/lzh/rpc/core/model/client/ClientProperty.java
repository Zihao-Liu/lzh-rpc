package com.lzh.rpc.core.model.client;


import java.util.List;

import com.lzh.rpc.core.client.balance.LoadBalanceStrategy;
import com.lzh.rpc.core.processor.BaseProcessor;
import com.lzh.rpc.core.serialize.strategy.SerializeStrategy;

/**
 * RPC服务调用者所需要的基本信配置信息
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class ClientProperty {
    /**
     * 服务提供者标识，自定义，不可重复
     */
    private String reference;

    /**
     * 服务提供者名称
     */
    private String service;
    /**
     * 服务注册发现方式，{@link com.lzh.rpc.core.constant.RegisterTypeEnum}.type
     */
    private String register;
    /**
     * 服务注册中心域名，直连时为ip地址
     */
    private String domain;
    /**
     * 负载均衡策略，值与{@link com.lzh.rpc.core.constant.BalanceStrategyEnum}
     * 中的strategy字段相匹配，优先级 < balanceStrategy 字段
     * 如果balanceStrategy为null，且该字段为空，会默认使用
     * {@link com.lzh.rpc.core.client.balance.RandomBalanceStrategy}
     */
    private String balance;

    /**
     * 请求用appId
     */
    private Integer appId;

    /**
     * 请求注册中心时的token
     */
    private String appToken;

    /**
     * 请求Provider时的token
     */
    private String accessToken;

    /**
     * 服务发现间隔(ms)，默认为15000ms，取决于注册
     * 中心过期时间
     */
    private Integer duration;

    /**
     * 序列化方式，值与{@link com.lzh.rpc.core.constant.SerializeStrategyEnum}
     * 中的type字段相匹配，优先级 < serializeClass 字段
     * serializeClass，且该字段为空，会默认使用
     * {@link com.lzh.rpc.core.serialize.strategy.ProtoStuffSerializeStrategy}
     */
    private String serialize;

    /**
     * Rpc请求设置的超时时间
     */
    private Integer timeout;

    /**
     * 显式传递序列化实现类，适用于自定义序列化的方式。优先级>serialize字段
     * 如果这个字段值为null，那么会使用serialize设置的方式
     */
    private Class<? extends SerializeStrategy> serializeClass;

    /**
     * 显式传递负载均衡实现类，适用于自定义负载均衡的方式。优先级>balance字段
     * 如果这个字段值为null，那么会使用balance设置的方式
     */
    private LoadBalanceStrategy balanceStrategy;

    private List<Class<? extends BaseProcessor>> processors;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public Class<? extends SerializeStrategy> getSerializeClass() {
        return serializeClass;
    }

    public void setSerializeClass(Class<? extends SerializeStrategy> serializeClass) {
        this.serializeClass = serializeClass;
    }

    public LoadBalanceStrategy getBalanceStrategy() {
        return balanceStrategy;
    }

    public void setBalanceStrategy(LoadBalanceStrategy balanceStrategy) {
        this.balanceStrategy = balanceStrategy;
    }

    public List<Class<? extends BaseProcessor>> getProcessors() {
        return processors;
    }

    public void setProcessors(List<Class<? extends BaseProcessor>> processors) {
        this.processors = processors;
    }
}
