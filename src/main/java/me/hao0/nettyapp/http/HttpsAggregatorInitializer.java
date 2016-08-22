package me.hao0.nettyapp.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Http Aggregator Initializer:
 * 1. HTTP消息自动聚合;
 * 2. 支持HTTP压缩;
 * 3. 支持SSL.
 */
public class HttpsAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    private final SslContext context;

    public HttpsAggregatorInitializer(SslContext context, boolean client) {
        this.client = client;
        this.context = context;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = context.newEngine(ch.alloc());
        pipeline.addFirst("ssl", new SslHandler(engine));

        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
            pipeline.addLast("decompressor",new HttpContentDecompressor());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
            pipeline.addLast("compressor",new HttpContentCompressor());
        }
        // 添加HttpObjectAggregator到ChannelPipeline,使用最大消息值是512kb
        pipeline.addLast("aggegator", new HttpObjectAggregator(512 * 1024));
    }
}