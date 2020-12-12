package com.lzh.rpc.center.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lzh.rpc.center.model.entity.AppMetaInfoEntity;
import com.lzh.rpc.center.service.AppService;
import com.lzh.rpc.center.service.InstanceService;
import com.lzh.rpc.common.constant.RpcErrorEnum;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.center.RegisterHeader;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.common.util.HostUtil;
import com.lzh.rpc.common.util.SignUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Liuzihao
 * Created on 2020-11-11
 */
@Slf4j
@Service
public class InstanceServiceImpl implements InstanceService {

    @Resource
    private AppService appService;

    private static final Map<String, Map<String, Long>> APP_INSTANCE_MAP = Maps.newConcurrentMap();
    private static final Map<String, Set<String>> APP_FORBID_MAP = Maps.newConcurrentMap();
    private static final long VALID_LIVE_TIME = 1000 * 30;


    @Override
    public Boolean register(ProviderInstance instance, RegisterHeader header) {
        log.info("register, param: {}, header: {}", instance, header);

        AppMetaInfoEntity appEntity = appService.getAppById(header.getAppId());
        checkParam(appEntity, header, instance, instance.getLastLiveTime());

        String instanceKey = HostUtil.formatAddress(instance.getHost(), instance.getPort());

        Map<String, Long> instanceMap = APP_INSTANCE_MAP.getOrDefault(appEntity.getAppName(), Maps.newConcurrentMap());
        instanceMap.put(instanceKey, instance.getLastLiveTime());
        APP_INSTANCE_MAP.merge(appEntity.getAppName(), instanceMap, (oldValue, newValue) -> {
            oldValue.putAll(newValue);
            return oldValue;
        });
        return true;
    }

    @Override
    public Boolean destroy(ProviderInstance instance, RegisterHeader header) {
        log.info("destroy, param: {}, header: {}", instance, header);

        AppMetaInfoEntity appEntity = appService.getAppById(header.getAppId());
        checkParam(appEntity, header, instance, instance.getLastLiveTime());

        String instanceKey = HostUtil.formatAddress(instance.getHost(), instance.getPort());
        APP_INSTANCE_MAP.computeIfPresent(appEntity.getAppName(), (key, oldValue) -> {
            oldValue.remove(instanceKey);
            return oldValue;
        });
        return true;
    }

    @Override
    public List<ProviderInstance> discover(String providerName, RegisterHeader consumerHeader) {
        log.info("discover, param: {}, header: {}", providerName, consumerHeader);
        AppMetaInfoEntity consumerEntity = appService.getAppById(consumerHeader.getAppId());
        checkParam(consumerEntity, consumerHeader, providerName, consumerHeader.getTimestamp());

        Map<String, Long> instanceMap = APP_INSTANCE_MAP.getOrDefault(providerName, Maps.newHashMap());
        Set<String> forbidInstance = APP_FORBID_MAP.getOrDefault(providerName, Sets.newHashSet());
        long discoverTime = consumerHeader.getTimestamp();

        return instanceMap.entrySet().stream()
                .filter(entry -> discoverTime - entry.getValue() <= VALID_LIVE_TIME)
                .filter(entry -> !forbidInstance.contains(entry.getKey()))
                .map(entry -> new ProviderInstance(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean manualForbid(String appName, String address, Integer operate) {
        if (operate > 0) {
            APP_FORBID_MAP.compute(address, (key, oldValue) -> {
                if (CollectionUtils.isEmpty(oldValue)) {
                    oldValue = Sets.newConcurrentHashSet();
                }
                oldValue.add(address);
                return oldValue;
            });
        } else {
            APP_FORBID_MAP.computeIfPresent(address, (key, oldValue) -> {
                oldValue.remove(address);
                return oldValue;
            });
        }

        return true;
    }

    private <T> void checkParam(AppMetaInfoEntity appEntity, RegisterHeader header, T param, Long lastLiveTime) {
        String sign = SignUtil.getSignMd5(header.getTimestamp(), header.getAppId(), appEntity.getAppToken(), param);

        boolean valid = sign.equals(header.getSign())
                && header.getTimestamp().equals(lastLiveTime);
        if (!valid) {
            throw RpcException.error(RpcErrorEnum.PERMISSION_DENIED);
        }
    }
}
