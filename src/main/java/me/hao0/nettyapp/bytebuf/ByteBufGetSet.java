package me.hao0.nettyapp.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.Charset;

/**
 * Author: haolin
 * Date:   8/13/16
 * Email:  haolin.h0@gmail.com
 */
public class ByteBufGetSet {

    public static void main(String[] args){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);    //1
        System.out.println((char)buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0, (byte)'B');
        System.out.println((char)buf.getByte(0));
        assert readerIndex == buf.readerIndex();
        assert writerIndex ==  buf.writerIndex();
    }
}
