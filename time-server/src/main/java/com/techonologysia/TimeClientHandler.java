package com.techonologysia;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * In a stream-based transport such as TCP/IP, received data is stored into a socket receive buffer.
 * Unfortunately, the buffer of a stream-based transport is not a queue of packets but a queue of bytes.
 * It means, even if you sent two messages as two independent packets, an operating system will not treat them as two messages but as just a bunch of bytes.
 * Therefore, there is no guarantee that what you read is exactly what your remote peer wrote
 */
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
