package me.hao0.nettyapp.chunk_data;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.io.File;
import java.io.FileInputStream;

/**
 * 如何使用 ChunkedWriteHandler 编写大型数据而避免 OutOfMemoryErrors 错误
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {
    private final File file;
    private final SslContext sslCtx;

    public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
        this.file = file;
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加SslHandler到ChannelPipeline.
        pipeline.addLast(new SslHandler(sslCtx.newEngine(ch.alloc())));
        // 添加ChunkedWriteHandler用来处理作为ChunkedInput传进的数据
        pipeline.addLast(new ChunkedWriteHandler());
        // 连接建立时,WriteStreamHandler开始写文件的内容
        pipeline.addLast(new WriteStreamHandler());
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}