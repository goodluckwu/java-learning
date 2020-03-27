package com.technologysia;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuzhihao
 */
public class EchoInBoundHandler1 extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(EchoInBoundHandler1.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf read = (ByteBuf) msg;
        byte[] bytes = new byte[read.readableBytes()];
        read.readBytes(bytes);
        String str = new String(bytes);
        log.info("EchoInBoundHandler1 received-[{}]", str);

        read.writeBytes(str.getBytes());
        ctx.fireChannelRead(read);

    }
}
