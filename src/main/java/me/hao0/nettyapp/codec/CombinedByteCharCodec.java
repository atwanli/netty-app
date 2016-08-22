package me.hao0.nettyapp.codec;

import io.netty.channel.CombinedChannelDuplexHandler;
import me.hao0.nettyapp.codec.decoder.ByteToCharDecoder;
import me.hao0.nettyapp.codec.encoder.CharToByteEncoder;

public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }

}