package me.hao0.nettyapp.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * Author: haolin
 * Date:   8/22/16
 * Email:  haolin.h0@gmail.com
 */
public class LogEventBroadcaster {

    private final Bootstrap bootstrap;

    private final File file;

    private final EventLoopGroup group;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws IOException {
        Channel ch = bootstrap.bind(0).syncUninterruptibly().channel();
        System.out.println("LogEventBroadcaster running");
        long pointer = 0;

        for (;;){
            long len = file.length();
            if (len < pointer) {
                // file was reset
                pointer = len; //3
            } else if (len > pointer) {
                // Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);  //4
                String line;
                while ((line = raf.readLine()) != null) {
                    ch.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer(); //6
                raf.close();
            }

            try {
                Thread.sleep(1000);  //7
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }


    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255",
                Integer.parseInt(args[0])), new File(args[1]));  //8
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}
