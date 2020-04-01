package com.technologysia;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuzhihao
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            log.info("服务端关闭连接");
            ctx.close();
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("From Client - {}", msg);
        if("PING".equals(msg)){
            ctx.writeAndFlush(Unpooled.copiedBuffer("PONG", CharsetUtil.UTF_8));
        }else {
            ctx.writeAndFlush(Unpooled.copiedBuffer("World", CharsetUtil.UTF_8));
        }
    }
}
