package io.github.wuzhihao7.netty.time.demo6.server;

import io.github.wuzhihao7.demo6.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        UnixTime time = (UnixTime) msg;
        ByteBuf buf = ctx.alloc().buffer(8);
        buf.writeLong(time.value());
        ctx.write(buf, promise);
    }
}
