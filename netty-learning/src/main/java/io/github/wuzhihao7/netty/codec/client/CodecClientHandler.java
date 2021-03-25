package io.github.wuzhihao7.netty.codec.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class CodecClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        LongStream.range(0,5).forEach(i -> {
//            ByteBuf buffer = ctx.alloc().buffer();
//            buffer.writeLong(i);
//            ctx.writeAndFlush(buffer);
//        });
        List<ChannelFuture> list = new ArrayList<>(5);
        LongStream.range(0, 5).forEach(i -> {
            byte[] bytes = "abcdabcdabcd".getBytes(StandardCharsets.UTF_8);
            System.out.println(bytes.length);
            ChannelFuture f = ctx.writeAndFlush(Unpooled.copiedBuffer(bytes));
            list.add(f);
        });

        list.forEach(f -> f.addListener(ChannelFutureListener.CLOSE));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
