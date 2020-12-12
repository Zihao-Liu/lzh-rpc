package com.lzh.rpc.core.client.balance;

import static com.lzh.rpc.core.constant.BalanceStrategyEnum.RANDOM;
import static com.lzh.rpc.core.constant.BalanceStrategyEnum.strategyOf;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.core.constant.BalanceStrategyEnum;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.client.ClientProperty;

/**
 * 负载均衡的抽象类，默认支持3种策略方式
 * <p>
 * 1. Random: 随机调度，会随机选取服务下任意一个可用的实例。也是默认的策略方式
 * 2. RoundRobin: 轮询调度，并不是严格轮询，实现方式依赖LinkedHashMap维护的顺序关系，
 * 实例的增加/删除也可能导致调度顺序改变
 * 3. IpHash: 简单Ip哈希，通过调用方的Ip地址进行哈希的算法，不推荐使用
 * <p>
 * 目前没有考虑跨机房，跨集群的加权调度算法，如果有需要。可以通过继承抽象类，自定义策略
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public abstract class AbstractBalanceStrategy implements LoadBalanceStrategy {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(AbstractBalanceStrategy.class);

    public static LoadBalanceStrategy getStrategy(ClientProperty property) {
        if (Objects.nonNull(property.getBalanceStrategy())) {
            // 如果由外界显式传进来自定义实现的负载均衡类，优先使用
            return property.getBalanceStrategy();
        }

        if (StringUtils.isBlank(property.getBalance())) {
            // 使用随机方式作为默认负载均衡策略
            LOGGER.debug("balance strategy is blank, use [random] as default");
            return RANDOM.getInstance();
        }

        BalanceStrategyEnum strategy = strategyOf(property.getBalance());
        if (Objects.isNull(strategy)) {
            LOGGER.error("balance strategy: [{}] not exist", property.getBalance());
            throw RpcException.error("balance strategy: [%s] not exist", property.getBalance());
        }
        return strategy.getInstance();
    }
}
