package com.lzh.rpc.core.provider.register;

import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.provider.ProviderProperty;
import com.lzh.rpc.core.constant.RegisterTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Liuzihao
 */
public abstract class AbstractProviderRegister implements RpcProviderRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProviderRegister.class);


    public static RpcProviderRegister getRegister(ProviderProperty property) {
        if (StringUtils.isEmpty(property.getRegister())) {
            LOGGER.info("provider register type is not set, use default type: [direct]");
            return RegisterTypeEnum.DIRECT.getProviderRegister();
        }
        RegisterTypeEnum register = RegisterTypeEnum.typeOf(property.getRegister().toLowerCase());
        if (Objects.isNull(register)) {
            LOGGER.error("provider register type [{}] dose not exist", property.getRegister());
            throw RpcException.error("provider register type [" + property.getRegister() + "] dose not exist");
        }
        LOGGER.info("provider register type set to: [{}]", property.getRegister());
        return register.getProviderRegister();
    }
}
