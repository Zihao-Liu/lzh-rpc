package com.lzh.rpc.core.client.factory;

import static com.lzh.rpc.common.constant.CommonConstant.ADDRESS_FORMAT;
import static com.lzh.rpc.common.constant.CommonConstant.BANNER_INFO;
import static com.lzh.rpc.common.constant.CommonConstant.DEFAULT_SERVER_ALIVE_MILLS;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.lang.NonNull;

import com.google.common.collect.Maps;
import com.lzh.rpc.common.model.client.ClientInvokeInfo;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.common.util.JsonUtil;
import com.lzh.rpc.core.client.discover.AbstractClientDiscover;
import com.lzh.rpc.core.client.net.RpcClientNetFactory;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.client.ClientProperty;

/**
 * @author Liuzihao
 * @since 0.0.1
 */
public abstract class BaseClientFactory extends InstantiationAwareBeanPostProcessorAdapter {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(BaseClientFactory.class);

    private static final Map<String, LinkedHashMap<String, ClientInvokeInfo>> DISCOVER_MAP = Maps.newConcurrentMap();

    public static void updateProvider(@NonNull String providerName, List<ProviderInstance> sourcePropertyList) {
        LOGGER.debug("update provider property, name: {}, sourceList: {}", providerName,
                JsonUtil.toJson(sourcePropertyList));
        providerName = providerName.toLowerCase();
        LinkedHashMap<String, ClientInvokeInfo> map = DISCOVER_MAP.getOrDefault(providerName, Maps.newLinkedHashMap());
        LinkedHashMap<String, ClientInvokeInfo> newMap = new LinkedHashMap<>(16, 0.75F, true);
        sourcePropertyList.forEach(property -> {
            String address = String.format(ADDRESS_FORMAT, property.getHost(), property.getPort());
            ClientInvokeInfo invokeInfo = map.getOrDefault(address, new ClientInvokeInfo());
            map.remove(address);
            invokeInfo.setDiscoverTime(property.getLastLiveTime());
            newMap.put(address, invokeInfo);
        });
        DISCOVER_MAP.put(providerName, newMap);
        String finalProviderName = providerName;
        map.forEach((address, invokeInfo) -> RpcClientNetFactory.close(finalProviderName, address));
    }


    public static Map<String, ClientInvokeInfo> getSourceMap(String providerName) {
        Map<String, ClientInvokeInfo> invokeInfoMap = DISCOVER_MAP.getOrDefault(providerName.toLowerCase(), Maps.newLinkedHashMap());
        Map<String, ClientInvokeInfo> resultMap = Maps.newHashMap();
        long currentTime = System.currentTimeMillis();
        invokeInfoMap.forEach((key, value) -> {
            if (value.getDiscoverTime() + DEFAULT_SERVER_ALIVE_MILLS > currentTime) {
                resultMap.put(key, value);
            }
        });
        return resultMap;
    }

    abstract List<ClientProperty> listClientProperty();

    public void start() {
        List<ClientProperty> sourceList = this.listClientProperty();
        if (CollectionUtils.isNotEmpty(sourceList)) {
            LOGGER.info("{}", BANNER_INFO);
            AbstractClientDiscover.discover(sourceList);
        }
    }

    public void stop() {
        AbstractClientDiscover.destroy();
        RpcClientNetFactory.clear();
    }
}
