package com.lzh.rpc.core.serialize.encoder;

import com.lzh.rpc.core.serialize.util.HessianSerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Liuzihao
 */
public class HessianEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public HessianEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf out) {
        //序列化
        if (genericClass.isInstance(obj)) {
            byte[] data = HessianSerializeUtil.serialize(obj);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
