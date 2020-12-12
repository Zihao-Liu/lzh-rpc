package com.lzh.rpc.core.client.factory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.lzh.rpc.core.annotation.RpcClient;
import com.lzh.rpc.core.client.net.RpcClientLocalProxy;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.client.ClientProperty;

/**
 * @author Liuzihao
 */
public class RpcSpringClientFactory extends BaseClientFactory implements InitializingBean, DisposableBean {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(RpcSpringClientFactory.class);

    private final List<ClientProperty> clientPropertyList;

    public RpcSpringClientFactory(List<ClientProperty> clientPropertyList) {
        this.clientPropertyList = clientPropertyList;
    }

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {
        Class<?> objClz = bean.getClass();
        for (Field field : objClz.getDeclaredFields()) {
            RpcClient rpcClient = field.getAnnotation(RpcClient.class);
            if (null != rpcClient) {
                Class<?> type = field.getType();
                field.setAccessible(true);
                try {
                    ClientProperty clientProperty = getConsumerReferenceProperty(rpcClient);
                    if (Objects.isNull(clientProperty)) {
                        LOGGER.error("consumer property could not be null, reference [{}]", rpcClient.reference());
                        return false;
                    }
                    field.set(bean, RpcClientLocalProxy.invoke(type, clientProperty, rpcClient));
                    // TODO RpcClient增加warm up字段，避免lazy调用导致第一次rpc请求时间太长
                } catch (IllegalAccessException e) {
                    LOGGER.error("set consumer local proxy error, error:", e);
                    throw new BeanCreationException("set consumer local proxy error");
                }
                field.setAccessible(false);
            }
        }
        return super.postProcessAfterInstantiation(bean, beanName);
    }

    private ClientProperty getConsumerReferenceProperty(RpcClient rpcConsumer) {
        return clientPropertyList.stream()
                .filter(property -> property.getReference().equalsIgnoreCase(rpcConsumer.reference()))
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
    List<ClientProperty> listClientProperty() {
        return this.clientPropertyList;
    }
}
