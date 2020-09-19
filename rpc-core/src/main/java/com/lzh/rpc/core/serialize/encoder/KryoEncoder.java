package com.lzh.rpc.core.serialize.encoder;

import com.lzh.rpc.core.serialize.util.KryoSerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Liuzihao
 */
public class KryoEncoder extends MessageToByteEncoder {
    private Class<?> genericClass;

    public KryoEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf out) {
        //序列化
        if (genericClass.isInstance(obj)) {
            byte[] data = KryoSerializeUtil.serialize(obj);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
