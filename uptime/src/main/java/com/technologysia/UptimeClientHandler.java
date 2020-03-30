package com.technologysia;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author house
 */
@ChannelHandler.Sharable
public class UptimeClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(UptimeClientHandler.class);

    long startTime = -1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if(startTime < 0){
            startTime = System.currentTimeMillis();
        }
        println("Connected to:" + ctx.channel().remoteAddress());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(!(evt instanceof IdleStateEvent)){
            return;
        }
        IdleStateEvent event = (IdleStateEvent) evt;
        if(event.state() == IdleState.READER_IDLE){
            println("Disconnecting due to no inbound traffic");
            ctx.close();
        }
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        println("Disconnected from: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        println("Sleeping for: " + UptimeClient.RECONNECT_DELAY + 's');

        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                println("Reconnecting to: " + UptimeClient.HOST + ':' + UptimeClient.PORT);
                UptimeClient.connect();
            }
        }, UptimeClient.RECONNECT_DELAY, TimeUnit.SECONDS);
    }


    public void println(String msg){
        if(startTime < 0){
            System.err.format("[SERVER DOWN]%s%n", msg);
        }else {
            System.err.format("[UPTIME:%5ds]%s%n", (System.currentTimeMillis() - startTime) / 1000, msg);
        }
    }
}
