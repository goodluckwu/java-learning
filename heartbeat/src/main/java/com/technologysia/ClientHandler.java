package com.technologysia;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private final Client client;

    public ClientHandler(Client client){
        this.client = client;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> client.createBootstrap(eventLoop), 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            log.info("客户端发送心跳检测");
            ctx.writeAndFlush(Unpooled.copiedBuffer("PING", CharsetUtil.UTF_8));
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("From Server - [{}]", msg);
    }
}
