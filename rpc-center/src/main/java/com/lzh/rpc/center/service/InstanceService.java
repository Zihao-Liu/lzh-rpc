package com.lzh.rpc.center.service;

import java.util.List;

import com.lzh.rpc.common.model.center.RegisterHeader;
import com.lzh.rpc.common.model.server.ProviderInstance;

/**
 * @author Liuzihao
 * Created on 2020-11-11
 */
public interface InstanceService {
    Boolean register(ProviderInstance instance, RegisterHeader header);

    Boolean destroy(ProviderInstance instance, RegisterHeader header);

    List<ProviderInstance> discover(String providerName, RegisterHeader consumerHeader);

    Boolean manualForbid(String appName, String address, Integer operate);
}
