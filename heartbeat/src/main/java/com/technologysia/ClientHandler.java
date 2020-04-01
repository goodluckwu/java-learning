package com.technologysia;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

public class ClientHandler extends ChannelInboundHandlerAdapter {
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
}
