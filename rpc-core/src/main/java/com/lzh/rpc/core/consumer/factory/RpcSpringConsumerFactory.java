package com.lzh.rpc.core.consumer.factory;

import com.lzh.rpc.common.annotation.RpcConsumer;
import com.lzh.rpc.core.consumer.net.RpcConsumerLocalProxy;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.consumer.ConsumerProperty;
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

    private List<ConsumerProperty> consumerPropertyList;

    public RpcSpringConsumerFactory(List<ConsumerProperty> consumerPropertyList) {
        this.consumerPropertyList = consumerPropertyList;
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
                    field.set(bean, RpcConsumerLocalProxy.invoke(type, consumerProperty));
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
        super.start();
    }

    @Override
    public void destroy() {
        super.stop();
    }

    @Override
    List<ConsumerProperty> listConsumerProperty() {
        return this.consumerPropertyList;
    }
}
