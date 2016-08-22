package me.hao0.nettyapp.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * Author: haolin
 * Date:   8/13/16
 * Email:  haolin.h0@gmail.com
 */
public class ByteBufReadWrite {

    public static void main(String[] args){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        System.out.println((char)buf.readByte());

        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.writeByte((byte)'?');

        assert readerIndex == buf.readerIndex();
        assert writerIndex != buf.writerIndex();
    }
}
