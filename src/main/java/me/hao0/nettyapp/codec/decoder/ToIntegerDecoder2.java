package me.hao0.nettyapp.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/**
 * 无需作是否有足够字节的判断
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws Exception {
        out.add(in.readInt());
    }
}