package com.lzh.rpc.center.service;

import com.lzh.rpc.center.model.entity.AppMetaInfoEntity;

/**
 * @author Liuzihao
 * Created on 2020-11-11
 */
public interface AppService {
    Integer openApp(String appName, String appDesc);

    AppMetaInfoEntity getAppById(Integer appId);

    AppMetaInfoEntity getByAppName(String appName);
}
