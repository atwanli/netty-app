package me.hao0.nettyapp.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

/**
 * Server Bootstrap Starter
 */
public class ServerBootStrapMain {

    public static void main(String[] args) {

        NioEventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
            .channel(NioServerSocketChannel.class)
            .childHandler(new SimpleChannelInboundHandler<ByteBuf>() { //4
                  @Override
                  protected void channelRead0(ChannelHandlerContext ctx,
                                              ByteBuf byteBuf) throws Exception {
                      System.out.println("Reveived data");
                      byteBuf.clear();
                  }
        });

        ChannelFuture future = bootstrap.bind(new InetSocketAddress(22222));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bound attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
