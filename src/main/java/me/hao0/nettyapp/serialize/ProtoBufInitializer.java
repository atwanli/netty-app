package me.hao0.nettyapp.serialize;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;


public class ProtoBufInitializer extends ChannelInitializer<Channel> {

    private final MessageLite lite;

    public ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 添加ProtobufVarint32FrameDecoder用来分割帧
        pipeline.addLast(new ProtobufVarint32FrameDecoder());

        // 添加ProtobufEncoder用来处理消息的编码
        pipeline.addLast(new ProtobufEncoder());

        // 添加ProtobufDecoder用来处理消息的解码
        pipeline.addLast(new ProtobufDecoder(lite));

        // 添加ObjectHandler用来处理解码了的消息
        pipeline.addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            // Do something with the object
        }
    }
}