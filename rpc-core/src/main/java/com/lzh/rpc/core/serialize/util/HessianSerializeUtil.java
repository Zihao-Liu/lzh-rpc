package com.lzh.rpc.core.serialize.util;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.lzh.rpc.common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Liuzihao
 */
public class HessianSerializeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HessianSerializeUtil.class);

    private HessianSerializeUtil() {
    }

    public static <T> byte[] serialize(T obj) throws RpcException {
        Hessian2Output ho = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ho = new Hessian2Output(os);
            ho.writeObject(obj);
            ho.flush();
            return os.toByteArray();
        } catch (IOException e) {
            LOGGER.error("serialize error, caused by: ", e);
            throw RpcException.error("serialize error");
        } finally {
            if (ho != null) {
                try {
                    ho.close();
                } catch (IOException e) {
                    LOGGER.error("serialize error, caused by: ", e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes, Class<?> cls) throws RpcException {
        Hessian2Input hi = null;
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            hi = new Hessian2Input(is);
            return (T) hi.readObject(cls);
        } catch (Exception e) {
            LOGGER.error("deserialize error, caused by: ", e);
            throw RpcException.error();
        } finally {
            if (hi != null) {
                try {
                    hi.close();
                } catch (Exception e) {
                    LOGGER.error("deserialize error, caused by: ", e);
                }
            }
        }
    }
}
