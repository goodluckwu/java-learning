package com.techonologysia;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;

public class TimeClientHandler2 extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TimeClientHandler2.class);

    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //A ChannelHandler has two life cycle listener methods: handlerAdded() and handlerRemoved().
        // You can perform an arbitrary(任意的) (de)initialization task as long as it does not block for a long time.
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buf.release();
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        //First, all received data should be cumulated(累加) into buf.
        buf.writeBytes(m);
        m.release();

        //And then, the handler must check if buf has enough data, 4 bytes in this example, and proceed to the actual business logic.
        // Otherwise, Netty will call the channelRead() method again when more data arrives, and eventually all 4 bytes will be cumulated.
        if(buf.readableBytes() >= 4){
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            log.info("recv-[{}]", Instant.ofEpochMilli(currentTimeMillis).atZone(ZoneId.systemDefault()).toLocalDateTime());
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
