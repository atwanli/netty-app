package me.hao0.nettyapp.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;

/**
 * Skip too long frame
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
                       List<Object> out) throws Exception {
        int readable = in.readableBytes();
        if (readable > MAX_FRAME_SIZE) { //2
            in.skipBytes(readable);        //3
            throw new TooLongFrameException("Frame too big!");
        }
        // do something
    }
}