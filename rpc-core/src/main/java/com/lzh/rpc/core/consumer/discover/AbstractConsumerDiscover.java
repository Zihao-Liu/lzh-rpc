package com.lzh.rpc.core.consumer.discover;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.consumer.ConsumerProperty;
import com.lzh.rpc.core.constant.RegisterTypeEnum;
import com.lzh.rpc.core.log.LoggerAdapter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author Liuzihao
 */
public abstract class AbstractConsumerDiscover implements RpcConsumerDiscover {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(AbstractConsumerDiscover.class);

    public static void discover(List<ConsumerProperty> consumerPropertyList){
        Map<String, List<ConsumerProperty>> consumerMap = Maps.newHashMap();
        for (ConsumerProperty consumerProperty : consumerPropertyList) {
            consumerMap.computeIfAbsent(consumerProperty.getRegister(), k -> new ArrayList<>()).add(consumerProperty);
        }
        consumerMap.forEach((type, propertyList) -> {
            if (StringUtils.isBlank(type)) {
                RegisterTypeEnum.DIRECT.getConsumerDiscover().doDiscover(propertyList);
                return;
            }
            RegisterTypeEnum register = RegisterTypeEnum.typeOf(type);
            if (Objects.isNull(register)) {
                LOGGER.error("register type not exist, type: [{}]", type);
                throw RpcException.error("register type not exist");
            }
            register.getConsumerDiscover().doDiscover(propertyList);
        });
    }
    public static void destroy() {
        LOGGER.info("discover destroy");
    }

    ExecutorService getThreadPool(int coreSize) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("consumer-discover-thread-%d")
                .setDaemon(true).build();
        return new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(1), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
