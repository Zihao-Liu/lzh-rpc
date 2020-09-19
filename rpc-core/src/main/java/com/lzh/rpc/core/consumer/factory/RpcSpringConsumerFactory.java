package com.lzh.rpc.core.consumer.factory;

import com.lzh.rpc.common.annotation.RpcConsumer;
import com.lzh.rpc.common.model.consumer.ConsumerProperty;
import com.lzh.rpc.core.consumer.discover.AbstractConsumerDiscover;
import com.lzh.rpc.core.consumer.net.RpcConsumerLocalProxy;
import com.lzh.rpc.core.log.LoggerAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author Liuzihao
 */
public class RpcSpringConsumerFactory extends BaseConsumerFactory implements InitializingBean, DisposableBean {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(RpcSpringConsumerFactory.class);

    private static RpcConsumerLocalProxy rpcConsumerLocalProxy;

    static {
        rpcConsumerLocalProxy = new RpcConsumerLocalProxy();
    }

    public RpcSpringConsumerFactory(List<ConsumerProperty> propertyList) {
        BaseConsumerFactory.setConsumerPropertyList(propertyList);
    }

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {
        Class<?> objClz = bean.getClass();
        for (Field field : objClz.getDeclaredFields()) {
            RpcConsumer rpcConsumer = field.getAnnotation(RpcConsumer.class);
            if (null != rpcConsumer) {
                Class<?> type = field.getType();
                field.setAccessible(true);
                try {
                    ConsumerProperty consumerProperty = getConsumerReferenceProperty(rpcConsumer);
                    if (Objects.isNull(consumerProperty)) {
                        LOGGER.error("consumer property could not be null, reference [{}]", rpcConsumer.reference());
                        return false;
                    }
                    field.set(bean, rpcConsumerLocalProxy.invoke(type, consumerProperty));
                } catch (IllegalAccessException e) {
                    LOGGER.error("set consumer local proxy error, error:", e);
                    throw new BeanCreationException("set consumer local proxy error");
                }
                field.setAccessible(false);
            }
        }
        return super.postProcessAfterInstantiation(bean, beanName);
    }

    private ConsumerProperty getConsumerReferenceProperty(RpcConsumer rpcConsumer) {
        return consumerPropertyList.stream()
                .filter(property -> property.getRegistry().equalsIgnoreCase(rpcConsumer.reference()))
                .findFirst().orElse(null);
    }

    @Override
    public void afterPropertiesSet() {
        List<ConsumerProperty> sourceList = consumerPropertyList;
        if (CollectionUtils.isEmpty(sourceList)) {
            return;
        }
        AbstractConsumerDiscover.discover(consumerPropertyList);
        sourceList.forEach(source -> BaseConsumerFactory.setBalance(source.getRegistry(), source.getBalance()));
    }

    @Override
    public void destroy() {
        AbstractConsumerDiscover.destroy();
    }
}
