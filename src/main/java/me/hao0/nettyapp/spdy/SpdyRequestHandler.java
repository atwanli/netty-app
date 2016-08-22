package me.hao0.nettyapp.spdy;

import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
public class SpdyRequestHandler extends HttpRequestHandler {

    @Override
    protected String getContent() {
        return "This content is transmitted via SPDY\r\n";
    }

}