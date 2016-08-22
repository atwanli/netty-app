package me.hao0.nettyapp.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 用于处理命令行的HandlerInitializer
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new CmdDecoder(65 * 1024));
        pipeline.addLast(new CmdHandler());
    }

    /**
     * 从重写方法 decode() 中检索一行,并从其内容中构建一个 Cmd 的实 例
     */
    public static final class CmdDecoder extends LineBasedFrameDecoder {

        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            ByteBuf frame =  (ByteBuf) super.decode(ctx, buffer);
            if (frame == null) {
                return null;
            }

            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), (byte)' ');

            return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index +1, frame.writerIndex()));
        }
    }

    /**
     * 从 CmdDecoder 接收解码的 Cmd 对象和对它的一些处理
     */
    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            // Do something with the command
        }
    }

    /**
     * 存储帧的内容,其中一个 ByteBuf 用于存名字,另外一个存参数
     */
    public static class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;
        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }
        public ByteBuf name() {
            return name;
        }
        public ByteBuf args() {
            return args;
        }
    }
}
