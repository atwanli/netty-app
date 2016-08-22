package me.hao0.nettyapp.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 处理TextWebSocketFrame的Handler
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    /**
     * 覆写userEventTriggered() 方法来处理自定义事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 如果接收的事件表明握手成功,就从 ChannelPipeline 中删除HttpRequestHandler ,因为接下来不会接受HTTP 消息了
            ctx.pipeline().remove(HttpRequestHandler.class);

            // 写一条消息给所有的已连接 WebSocket 客户端,通知它们建立了一个新的 Channel 连接
            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));

            // 添加新连接的WebSocket Channel 到ChannelGroup中, 这样它就能收到所有的信息
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 保留收到的消息,并通过 writeAndFlush() 传递给所有连接的客户端。
        group.writeAndFlush(msg.retain());
    }
}