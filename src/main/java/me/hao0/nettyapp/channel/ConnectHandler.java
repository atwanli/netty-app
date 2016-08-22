package me.hao0.nettyapp.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Author: haolin
 * Date:   8/10/16
 * Email:  haolin.h0@gmail.com
 */
public class ConnectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // 当建立一个新的连接时调用 channelActive()

    }
}
