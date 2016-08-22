package me.hao0.nettyapp.channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * SimpleChannelInboundHandler可自动释放资源
 */
@ChannelHandler.Sharable
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> { //1

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             Object msg) {
    }
}