package com.lzh.rpc.core.server.register;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.core.constant.RegisterTypeEnum;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.server.ServerProperty;

/**
 * @author Liuzihao
 */
public abstract class AbstractServerRegister implements RpcServerRegister {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(HttpServerRegister.class);

    public static RpcServerRegister getRegister(ServerProperty property) {
        if (StringUtils.isEmpty(property.getRegister())) {
            LOGGER.info("provider register type is not set, use default type: [direct]");
            return RegisterTypeEnum.DIRECT.getRegister();
        }
        RegisterTypeEnum register = RegisterTypeEnum.typeOf(property.getRegister().toLowerCase());
        if (Objects.isNull(register)) {
            LOGGER.error("provider register type [{}] dose not exist", property.getRegister());
            throw RpcException.error("provider register type [" + property.getRegister() + "] dose not exist");
        }
        LOGGER.info("provider register type set to: [{}]", property.getRegister());
        return register.getRegister();
    }
}
