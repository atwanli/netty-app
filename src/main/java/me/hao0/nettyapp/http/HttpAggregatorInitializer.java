package me.hao0.nettyapp.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * Http Aggregator Initializer:
 * 1. HTTP消息自动聚合;
 * 2. 支持HTTP压缩;
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpAggregatorInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
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