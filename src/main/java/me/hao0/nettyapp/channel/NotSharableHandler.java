package me.hao0.nettyapp.channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 错误的状态共享
 */
@ChannelHandler.Sharable
public class NotSharableHandler extends ChannelInboundHandlerAdapter {
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        count++;
        System.out.println("inboundBufferUpdated(...) called the "
                + count + " time");
        ctx.fireChannelRead(msg);
    }
}