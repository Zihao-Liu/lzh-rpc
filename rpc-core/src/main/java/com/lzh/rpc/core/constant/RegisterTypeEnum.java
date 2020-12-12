package com.lzh.rpc.core.constant;

import com.lzh.rpc.core.client.discover.DirectClientDiscover;
import com.lzh.rpc.core.client.discover.HttpClientDiscover;
import com.lzh.rpc.core.client.discover.RpcClientDiscover;
import com.lzh.rpc.core.server.register.DirectServerRegister;
import com.lzh.rpc.core.server.register.HttpServerRegister;
import com.lzh.rpc.core.server.register.RpcServerRegister;

/**
 * @author Liuzihao
 * @since 0.0.1
 */
public enum RegisterTypeEnum {
    /**
     * 服务发现方式
     */
    DIRECT("direct", DirectServerRegister.getInstance(), DirectClientDiscover.getInstance()),
    HTTP("http", HttpServerRegister.getInstance(), HttpClientDiscover.getInstance()),
    ;

    private final String type;
    private final RpcServerRegister register;
    private final RpcClientDiscover discover;

    RegisterTypeEnum(String type, RpcServerRegister register, RpcClientDiscover clientDiscover) {
        this.type = type;
        this.register = register;
        this.discover = clientDiscover;
    }

    public String getType() {
        return type;
    }

    public RpcServerRegister getRegister() {
        return register;
    }

    public RpcClientDiscover getDiscover() {
        return discover;
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
