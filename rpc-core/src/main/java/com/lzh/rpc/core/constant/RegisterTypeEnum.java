package com.lzh.rpc.core.constant;

import com.lzh.rpc.core.consumer.discover.DirectConsumerDiscover;
import com.lzh.rpc.core.consumer.discover.RpcConsumerDiscover;
import com.lzh.rpc.core.provider.register.DirectProviderRegister;
import com.lzh.rpc.core.provider.register.RpcProviderRegister;

/**
 * @author Liuzihao
 */
public enum RegisterTypeEnum {
    /**
     * 服务发现方式
     */
    DIRECT("direct", DirectProviderRegister.init(), DirectConsumerDiscover.init()),
    ;

    private String type;
    private RpcProviderRegister providerRegister;
    private RpcConsumerDiscover consumerDiscover;

    RegisterTypeEnum(String type, RpcProviderRegister providerRegister, RpcConsumerDiscover consumerDiscover) {
        this.type = type;
        this.providerRegister = providerRegister;
        this.consumerDiscover = consumerDiscover;
    }

    public String getType() {
        return type;
    }

    public RpcProviderRegister getProviderRegister() {
        return providerRegister;
    }

    public RpcConsumerDiscover getConsumerDiscover() {
        return consumerDiscover;
    }

    public static RegisterTypeEnum typeOf(String type) {
        for (RegisterTypeEnum typeEnum : RegisterTypeEnum.values()) {
            if (typeEnum.type.equalsIgnoreCase(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}
