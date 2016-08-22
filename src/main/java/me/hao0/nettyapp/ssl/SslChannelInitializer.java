package me.hao0.nettyapp.ssl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;

    private final boolean startTls;

    private final boolean client;

    public SslChannelInitializer(SslContext context,
                                 boolean client, boolean startTls) {   //1
        this.context = context;
        this.startTls = startTls;
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(client);
        ch.pipeline()
            .addFirst("ssl", new SslHandler(engine, startTls)); //4
    }
}
 