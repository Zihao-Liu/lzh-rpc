package com.lzh.rpc.core.consumer.factory;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lzh.rpc.common.model.consumer.ConsumerInvokeInfo;
import com.lzh.rpc.common.model.provider.ProviderInstance;
import com.lzh.rpc.core.consumer.discover.AbstractConsumerDiscover;
import com.lzh.rpc.core.consumer.net.RpcConsumerClientFactory;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.consumer.ConsumerProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.lang.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.lzh.rpc.common.constant.CommonConstant.ADDRESS_FORMAT;
import static com.lzh.rpc.common.constant.CommonConstant.BANNER_INFO;

/**
 * @author Liuzihao
 * @since 0.0.1
 */
public abstract class BaseConsumerFactory extends InstantiationAwareBeanPostProcessorAdapter {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(BaseConsumerFactory.class);

    private static final Map<String, LinkedHashMap<String, ConsumerInvokeInfo>> SOURCE_DISCOVER_MAP = new ConcurrentHashMap<>();
    private static final Gson GSON = new Gson();

    public static void updateProvider(@NonNull String providerName, List<ProviderInstance> sourcePropertyList) {
        LOGGER.debug("update provider property, name: {}, sourceList: {}", providerName, GSON.toJson(sourcePropertyList));
        providerName = providerName.toLowerCase();
        LinkedHashMap<String, ConsumerInvokeInfo> map = SOURCE_DISCOVER_MAP.getOrDefault(providerName, Maps.newLinkedHashMap());
        LinkedHashMap<String, ConsumerInvokeInfo> newMap = new LinkedHashMap<>(16, 0.75F, true);
        long time = System.currentTimeMillis();
        sourcePropertyList.forEach(property -> {
            String address = String.format(ADDRESS_FORMAT, property.getHost(), property.getPort());
            ConsumerInvokeInfo invokeInfo = map.getOrDefault(address, new ConsumerInvokeInfo());
            map.remove(address);
            invokeInfo.setDiscoverTime(time);
            newMap.put(address, invokeInfo);
        });
        SOURCE_DISCOVER_MAP.put(providerName, newMap);
        String finalProviderName = providerName;
        map.forEach((address, invokeInfo) -> RpcConsumerClientFactory.close(finalProviderName, address));
    }


    public static Map<String, ConsumerInvokeInfo> getSourceMap(String providerName) {
        return SOURCE_DISCOVER_MAP.getOrDefault(providerName.toLowerCase(), Maps.newLinkedHashMap());
    }

    abstract List<ConsumerProperty> listConsumerProperty();

    public void start() {
        List<ConsumerProperty> sourceList = this.listConsumerProperty();
        if (CollectionUtils.isNotEmpty(sourceList)) {
            LOGGER.info("{}", BANNER_INFO);
            AbstractConsumerDiscover.discover(sourceList);
        }
    }

    public void stop() {
        AbstractConsumerDiscover.destroy();
        RpcConsumerClientFactory.clear();
    }
}
