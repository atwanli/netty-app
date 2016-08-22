package me.hao0.nettyapp.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * Client Bootstrap Starter
 */
public class ClientBootstrapMain {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                @Override
                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                    System.out.println("Received data");
                    msg.clear();
                }
            });

        ChannelFuture future = bootstrap.connect(
                new InetSocketAddress("localhost", 22222));

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture)
                    throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Connection established");
                } else {
                    System.err.println("Connection attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
