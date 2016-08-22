package me.hao0.nettyapp.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 *
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加一个LineBasedFrameDecoder用于提取帧并把数据包转发到下一个管道中的处理程序,
        // 在这种情况下就是 FrameHandler
        pipeline.addLast(new LineBasedFrameDecoder(65 * 1024));   //1
        pipeline.addLast(new FrameHandler());  //2
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception { //3
            // 每次调用都需要传递一个单帧的内容
        }
    }
}