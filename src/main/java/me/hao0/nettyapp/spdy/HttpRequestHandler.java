package me.hao0.nettyapp.spdy;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * 重写channelRead0(), 可以被所有的接收到的FullHttpRequest调用
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (HttpUtil.is100ContinueExpected(request)) {
            // 检查如果接下来的响应是预期的,就写入
            send100Continue(ctx);
        }

        // 新建FullHttpResponse,用于对请求的响应
        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
                HttpResponseStatus.OK);

        // 成响应的内容,将它写入payload
        response.content().writeBytes(getContent().getBytes(CharsetUtil.UTF_8));

        // 设置头文件,这样客户端就能知道如何与响应的payload交互
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UT F-8");

        // 检查请求设置是否启用了keepalive;如果是这样,将标题设置为符合HTTPRFC
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // 写响应给客户端,并获取到Future的引用,用于写完成时,获取到通知
        ChannelFuture future = ctx.writeAndFlush(response);
        if (!keepAlive) {
            // 如果响应不是keepalive,在写完成时关闭连接
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 返回内容作为响应的payload
     */
    protected String getContent() {
        return "This content is transmitted via HTTP\r\n";
    }

    /**
     * Helper方法生成了100持续的响应,并写回给客户端
     */
    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // 若执行阶段抛出异常,则关闭管道
        cause.printStackTrace();
        ctx.close();
    }
}