package com.lzh.rpc.core.server.factory;

import com.lzh.rpc.core.annotation.RpcServer;
import  com.lzh.rpc.core.model.server.ServerProperty;
import com.lzh.rpc.core.log.LoggerAdapter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StopWatch;

import java.net.UnknownHostException;
import java.util.Map;


/**
 * 支持以Spring的方式注册Rpc Provider Service
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class RpcSpringServerFactory extends BaseServerFactory
        implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(RpcSpringServerFactory.class);

    private final ServerProperty serverProperty;

    public RpcSpringServerFactory(ServerProperty serverProperty) {
        this.serverProperty = serverProperty;
    }

    /**
     * 扫描所有被HyRpcProvider修饰的类，并注册到RpcProviderFactory中
     *
     * @param applicationContext Spring上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // 获取所有被RpcProvider修饰的服务提供类
        Map<String, Object> providerServiceMap = applicationContext.getBeansWithAnnotation(RpcServer.class);
        if (MapUtils.isEmpty(providerServiceMap)) {
            LOGGER.info("does not find rpc provider service...");
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.info("rpc provider register begin ...");

        // 注入所有Provider实例
        providerServiceMap.forEach((name, instance) -> {
            RpcServer rpcProvider = applicationContext.findAnnotationOnBean(name, RpcServer.class);
            BaseServerFactory.putService(name, instance, rpcProvider);
        });
        stopWatch.stop();
        LOGGER.info("rpc provider register end ..., size:{}, cost: {}", BaseServerFactory.size(), stopWatch.getLastTaskTimeMillis());
    }

    @Override
    public void afterPropertiesSet() throws UnknownHostException {
        super.start();
    }

    @Override
    public void destroy() throws UnknownHostException {
        super.stop();
    }

    @Override
    ServerProperty getProperty() {
        return this.serverProperty;
    }
}
