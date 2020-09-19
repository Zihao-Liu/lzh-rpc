package com.lzh.rpc.core.serialize.decoder;

import com.lzh.rpc.core.serialize.util.KryoSerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author Liuzihao
 */
public class KryoDecoder extends ByteToMessageDecoder {

    public KryoDecoder(Class<?> genericClass) {
        // 兼容其他decoder用
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
        }
        //将ByteBuf转换为byte[]
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        //将data转换成object
        Object obj = KryoSerializeUtil.deserialize(data);
        out.add(obj);
    }
}

