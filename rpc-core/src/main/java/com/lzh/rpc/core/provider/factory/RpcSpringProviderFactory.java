package com.lzh.rpc.core.provider.factory;

import com.lzh.rpc.common.annotation.RpcProvider;
import com.lzh.rpc.common.model.provider.ProviderProperty;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.provider.register.AbstractProviderRegister;
import com.lzh.rpc.core.provider.server.NettyServer;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StopWatch;

import java.net.UnknownHostException;
import java.util.Map;

import static com.lzh.rpc.common.constant.CommonConstant.BANNER_INFO;


/**
 * @author Liuzihao
 * @since 0.0.1
 */
public class RpcSpringProviderFactory extends BaseProviderFactory implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(RpcSpringProviderFactory.class);

    public RpcSpringProviderFactory(ProviderProperty providerProperty) {
        BaseProviderFactory.setProviderProperty(providerProperty);
    }

    /**
     * 扫描所有被HyRpcProvider修饰的类，并注册到RpcProviderFactory中
     *
     * @param applicationContext Spring上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // 获取所有被RpcProvider修饰的服务提供类
        Map<String, Object> providerServiceMap = applicationContext.getBeansWithAnnotation(RpcProvider.class);
        if (MapUtils.isEmpty(providerServiceMap)) {
            LOGGER.info("does not find rpc provider service...");
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.info("rpc provider register begin ...");

        // 注入所有Provider实例
        providerServiceMap.forEach((name, instance) -> {
            RpcProvider rpcProvider = applicationContext.findAnnotationOnBean(name, RpcProvider.class);
            BaseProviderFactory.putService(name, instance, rpcProvider);
        });
        stopWatch.stop();
        LOGGER.info("rpc provider register end ..., size:{}, cost: {}", BaseProviderFactory.size(), stopWatch.getLastTaskTimeMillis());
    }

    @Override
    public void afterPropertiesSet() throws UnknownHostException {
        if (!BaseProviderFactory.isEmpty()) {
            LOGGER.info("{}", BANNER_INFO);
            BaseProviderFactory.setProviderProperty(providerProperty);
        }
        AbstractProviderRegister.getRegister(providerProperty).doRegister();
        new NettyServer().startNetty();
    }

    @Override
    public void destroy() throws UnknownHostException {
        AbstractProviderRegister.getRegister(providerProperty).doDestroy();
    }
}
