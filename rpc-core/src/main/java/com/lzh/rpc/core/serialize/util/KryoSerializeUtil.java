package com.lzh.rpc.core.serialize.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Liuzihao
 */
public class KryoSerializeUtil {
    private KryoSerializeUtil() {
    }

    public static byte[] serialize(Object obj) {
        Kryo kryo = KRYO_THREAD_LOCAL.get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) {
        Kryo kryo = KRYO_THREAD_LOCAL.get();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        input.close();
        return (T) kryo.readClassAndObject(input);
    }

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        // TODO Lzh remove?
        Kryo kryo = new Kryo();
        kryo.setReferences(true);// 默认值为 true, 强调作用
        kryo.setRegistrationRequired(false);// 默认值为 false, 强调作用
        return kryo;
    });
}
