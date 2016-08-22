package me.hao0.nettyapp.codec.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

/**
 * 将Integer消息编码为String消息
 */
public class IntegerToStringEncoder extends
        MessageToMessageEncoder<Integer> { //1
    @Override
    public void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out)
            throws Exception {
        out.add(String.valueOf(msg));  //2
    }
}