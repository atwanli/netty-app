package me.hao0.nettyapp.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class ByteToCharDecoder extends
        ByteToMessageDecoder { //1
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws Exception {
        if (in.readableBytes() >= 2) {  //2
            out.add(in.readChar());
        }
    }
}