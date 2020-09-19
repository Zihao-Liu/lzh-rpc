package com.lzh.rpc.core.provider.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Liuzihao
 */
public class DirectProviderRegister extends AbstractProviderRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectProviderRegister.class);

    private static volatile DirectProviderRegister instance = null;

    public static DirectProviderRegister init() {
        if (Objects.isNull(instance)) {
            synchronized (DirectProviderRegister.class) {
                if (Objects.isNull(instance)) {
                    instance = new DirectProviderRegister();
                }
            }
        }
        return instance;
    }

    @Override
    public void doRegister() {
        LOGGER.info("provider register type set to [direct], noting need to do");
    }

    @Override
    public void doDestroy() {
        LOGGER.info("provider register type set to [direct], noting need to do");
    }
}
