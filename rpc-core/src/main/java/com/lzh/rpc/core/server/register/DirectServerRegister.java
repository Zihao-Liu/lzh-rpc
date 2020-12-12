package com.lzh.rpc.core.server.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lzh.rpc.core.model.server.ServerProperty;

/**
 * @author Liuzihao
 */
public class DirectServerRegister extends AbstractServerRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectServerRegister.class);

    private static final DirectServerRegister INSTANCE = new DirectServerRegister();

    public static RpcServerRegister getInstance() {
        return INSTANCE;
    }

    @Override
    public void doRegister(ServerProperty property) {
        LOGGER.info("provider register type set to [direct], noting need to do");
    }

    @Override
    public void doDestroy(ServerProperty property) {
        LOGGER.info("provider register type set to [direct], noting need to do");
    }
}
