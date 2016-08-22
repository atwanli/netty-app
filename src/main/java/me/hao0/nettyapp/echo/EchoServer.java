package me.hao0.nettyapp.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Author: haolin
 * Date:   8/10/16
 * Email:  haolin.h0@gmail.com
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public void start() throws InterruptedException {

        // 创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {

            // 创建ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 配置ServerBootstrap
            bootstrap.group(group)
                // 指定使用 NIO 的传输 Channel
                .channel(NioServerSocketChannel.class)
                // 设置 socket 地址使用所选的端口
                .localAddress(new InetSocketAddress(this.port))
                // 当一个新的连接被接后, 一个新的子Channel将被创建,
                .childHandler(new ChannelInitializer(){
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // 添加 EchoServerHandler 到 Channel 的 ChannelPipeline的尾部
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                });

            // 绑定服务器, 并等待绑定完成
            ChannelFuture f = bootstrap.bind().sync();

            System.out.println(EchoServer.class.getName()
                    + " started and listen on " + f.channel().localAddress());
            // 等待Channel关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup,释放所有资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        if(args.length != 1){
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                            " <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        new EchoServer(port).start();
    }
}
