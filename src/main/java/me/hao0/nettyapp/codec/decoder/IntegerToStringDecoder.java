package me.hao0.nettyapp.codec.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

/**
 * Integer msg to String msg Decoder
 */
public class IntegerToStringDecoder extends
        MessageToMessageDecoder<Integer> {

    @Override
    public void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out)
            throws Exception {
        out.add(String.valueOf(msg));
    }

}