package com.technologysia;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int)m.getValue());
        //There are quite a few important things in this single line.
        //First, we pass the original ChannelPromise as-is so that Netty marks it as success or failure when the encoded data is actually written out to the wire.
        //Second, we did not call ctx.flush(). There is a separate handler method void flush(ChannelHandlerContext ctx) which is purposed to override the flush() operation.
        ctx.write(encoded, promise);

    }
}
