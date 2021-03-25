package io.github.wuzhihao7.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class LongDecoder extends ByteToMessageDecoder {
    private int count;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() < 8){
            return;
        }

        System.out.println("LongDecoder.decode called :" + (++this.count) + ", buf length: " + in.readableBytes());

        out.add(in.readBytes(8));

    }
}
