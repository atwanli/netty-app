package me.hao0.nettyapp.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Author: haolin
 * Date:   8/18/16
 * Email:  haolin.h0@gmail.com
 */
public class ChannelBootstrap {

    public static void main(String[] args) {

        // 创建一个新的ServerBootstrap来创建新的SocketChannel管道并且绑定他们
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 指定EventLoopGroups从ServerChannel和接收到的管道来注册并获取EventLoops
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                // 指定顶级Channel类来使用
                .channel(NioServerSocketChannel.class)
                // 用于子Channel的Handler, 即用于处理接收到的管道的I/O和数据
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {

                    ChannelFuture connectFuture;

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

                        // 创建一个新的Bootstrap来连接到远程主机
                        Bootstrap bootstrap = new Bootstrap();

                        // 设置管道类
                        bootstrap.channel(NioSocketChannel.class)
                                // 设置处理器来处理I/O
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                        System.out.println("Reveived data: " + msg.toString());
                                    }
                                });
                        // 共享该Channel的EventLoop
                        bootstrap.group(ctx.channel().eventLoop());
                        // 连接到远端
                        connectFuture = bootstrap.connect(new InetSocketAddress("www.google.com", 80));
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        if (connectFuture.isDone()) {
                            // 连接完成处理业务逻辑(比如,proxy)
                        }
                    }
                });
        // 绑定Server端口, 并启动
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
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
