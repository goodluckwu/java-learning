package com.techonologysia;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TimeClientHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //In TCP/IP, Netty reads the data sent from a peer into a ByteBuf.Netty将从对等节点发送的数据读入ByteBuf
        ByteBuf m = (ByteBuf) msg;
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            LocalDateTime localDateTime = Instant.ofEpochMilli(currentTimeMillis).atZone(ZoneId.systemDefault()).toLocalDateTime();
            log.info("Time Server Return - [{}]", localDateTime);
            ctx.close();
        }finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
