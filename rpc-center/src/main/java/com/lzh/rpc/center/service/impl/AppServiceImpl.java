package com.lzh.rpc.center.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.lzh.rpc.center.model.entity.AppMetaInfoEntity;
import com.lzh.rpc.center.service.AppService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Liuzihao
 * Created on 2020-11-11
 */
@Slf4j
@Service
public class AppServiceImpl implements AppService {

    private static final Map<String, AppMetaInfoEntity> APP_MAP = Maps.newConcurrentMap();
    private static final AtomicInteger APP_ID = new AtomicInteger(1);

    @Override
    public Integer openApp(String appName, String appDesc) {
        String token = UUID.randomUUID().toString().replace("-", "");
        int appId = APP_ID.getAndIncrement();
        AppMetaInfoEntity entity = AppMetaInfoEntity.builder()
                .id(appId)
                .appId(appId)
                .appName(appName)
                .appDesc(StringUtils.isBlank(appDesc) ? StringUtils.EMPTY : appDesc)
                .appToken(token)
                .build();
        AppMetaInfoEntity old = APP_MAP.putIfAbsent(appName, entity);
        return Objects.isNull(old) ? appId : 0;
    }

    @Override
    public AppMetaInfoEntity getAppById(Integer appId) {
        return APP_MAP.values().stream()
                .filter(entity -> entity.getAppId().equals(appId))
                .findFirst().orElse(null);
    }

    @Override
    public AppMetaInfoEntity getByAppName(String appName) {
        return APP_MAP.get(appName);
    }
}
