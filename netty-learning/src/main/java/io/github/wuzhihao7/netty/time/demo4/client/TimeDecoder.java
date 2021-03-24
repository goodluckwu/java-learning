package io.github.wuzhihao7.netty.time.demo4.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TimeDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("TimeDecoder.decode");
        if(in.readableBytes() < 8){
            return;
        }

        out.add(in.readBytes(8));
    }
}
