package com.lzh.rpc.core.consumer.discover;

import com.lzh.rpc.core.model.consumer.ConsumerProperty;;
import com.lzh.rpc.common.model.provider.ProviderInstance;
import com.lzh.rpc.core.consumer.factory.BaseConsumerFactory;
import com.lzh.rpc.core.log.LoggerAdapter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lzh.rpc.common.constant.CommonConstant.COMMON_SPLIT;
import static com.lzh.rpc.common.constant.CommonConstant.IP_AND_PORT_SPLIT;
import static com.lzh.rpc.core.constant.RegisterTypeEnum.DIRECT;

/**
 * @author Liuzihao
 */
public class DirectConsumerDiscover extends AbstractConsumerDiscover {
    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(DirectConsumerDiscover.class);

    private static volatile DirectConsumerDiscover instance = null;

    public static DirectConsumerDiscover init() {
        if (Objects.isNull(instance)) {
            synchronized (DirectConsumerDiscover.class) {
                if (Objects.isNull(instance)) {
                    instance = new DirectConsumerDiscover();
                }
            }
        }
        return instance;
    }

    @Override
    public void doDestroy() {
        LOGGER.info("discover destroy");
    }

    @Override
    public void doDiscover(List<ConsumerProperty> consumerPropertyList) {
        consumerPropertyList.stream()
                .filter(property -> StringUtils.isBlank(property.getRegister()) || DIRECT.getType().equalsIgnoreCase(property.getRegister()))
                .forEach(source -> {
                    String[] addressList = source.getDomain().split(COMMON_SPLIT);
                    List<ProviderInstance> sourceInfoList = Arrays.stream(addressList).map(address -> {
                        String[] hostAndPort = address.split(IP_AND_PORT_SPLIT);
                        return new ProviderInstance(hostAndPort[0], Integer.valueOf(hostAndPort[1]));
                    }).collect(Collectors.toList());
                    BaseConsumerFactory.updateProvider(source.getRegistry(), sourceInfoList);
                });
    }
}
