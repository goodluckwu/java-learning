package com.technologysia;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HeartBeatClientHandler.class);
    private static final int MAX_FAIL_TIMES = 3;

    private int failTimes = 0;
    private final HeartBeatClient heartBeatClient;

    public HeartBeatClientHandler(HeartBeatClient heartBeatClient){
        this.heartBeatClient = heartBeatClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        TimeUnit.SECONDS.sleep(10);
        if(evt instanceof IdleStateEvent){
            log.info("客户端发送心跳检测");
            ChannelFuture f = ctx.writeAndFlush(Unpooled.copiedBuffer("ping", CharsetUtil.UTF_8));
            f.addListener((ChannelFutureListener) future -> {
                if(future.isSuccess()){
                    failTimes = 0;
                }else{
                    failTimes++;
                    if(failTimes >= MAX_FAIL_TIMES){
                        //重连
                        log.info("客户端重连。。。");
                        heartBeatClient.start();
                    }
                }
            });
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(),cause);
        ctx.close();
    }
}
