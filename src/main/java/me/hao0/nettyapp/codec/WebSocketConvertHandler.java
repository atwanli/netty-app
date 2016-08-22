package me.hao0.nettyapp.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * Author: haolin
 * Date:   8/14/16
 * Email:  haolin.h0@gmail.com
 */
public class WebSocketConvertHandler extends
        MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {

    public static final WebSocketConvertHandler INSTANCE = new WebSocketConvertHandler();

    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.getData().duplicate().retain();
        switch (msg.getType()) {   //2
            case BINARY:
                out.add(new BinaryWebSocketFrame(payload));
                break;
            case TEXT:
                out.add(new TextWebSocketFrame(payload));
                break;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, payload));
                break;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(payload));
                break;
            case PONG:
                out.add(new PongWebSocketFrame(payload));
                break;
            case PING:
                out.add(new PingWebSocketFrame(payload));
                break;
            default:
                throw new IllegalStateException("Unsupported websocket msg " + msg);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) { //3
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY, msg.content().copy()));
        } else if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, msg.content().copy()));
        } else if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING, msg.content().copy()));
        } else if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG, msg.content().copy()));
        } else if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT, msg.content().copy()));
        } else if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION, msg.content().copy()));
        } else {
            throw new IllegalStateException("Unsupported websocket msg " + msg);
        }
    }

    public static final class MyWebSocketFrame{

        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf data;
        public MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }
        public FrameType getType() {
            return type;
        }
        public ByteBuf getData() {
            return data;
        }
    }
}
