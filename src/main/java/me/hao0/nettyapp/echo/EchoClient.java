package me.hao0.nettyapp.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * Author: haolin
 * Date:   8/10/16
 * Email:  haolin.h0@gmail.com
 */
public class EchoClient {

    private final String host;

    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
             .channel(NioSocketChannel.class)
             .remoteAddress(new InetSocketAddress(host, port))
             .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch)
                        throws Exception {
                    // 定制Handler
                    ch.pipeline().addLast(new EchoClientHandler());
                }
             });

            // 等待连接
            ChannelFuture f = bootstrap.connect().sync();

            // 等待关闭
            f.channel().closeFuture().sync();

        } finally {
            // 关闭线程池和, 释放所有资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(
                    "Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
            return;
        }
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }

}
