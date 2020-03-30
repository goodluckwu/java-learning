package com.technologysia;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author house
 */
@ChannelHandler.Sharable
public class UptimeServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(UptimeServerHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
