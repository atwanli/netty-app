package me.hao0.nettyapp.idle_timeout;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import java.util.concurrent.TimeUnit;

/**
 * 当超过60秒没有数据收到时,就会得到通知,
 * 此时就发送心跳到远端,如果没有回应,连接就关闭
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // IdleStateHandler将通过IdleStateEvent调用userEventTriggered,如果连接没有接收 或发送数据超过60秒钟
        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        pipeline.addLast(new HeartbeatHandler());
    }

    public static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {

        // 心跳发送到远端
        private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                // 发送的心跳并添加一个侦听器,如果发送操作失败将关闭连接
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}