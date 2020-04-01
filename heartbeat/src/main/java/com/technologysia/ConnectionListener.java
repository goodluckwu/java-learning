package com.technologysia;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectionListener implements ChannelFutureListener {
    private static final Logger log = LoggerFactory.getLogger(ConnectionListener.class);

    private final Client client;

    public ConnectionListener(Client client){
        this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(!future.isSuccess()){
            log.info("Reconnect");
            EventLoop eventLoop = future.channel().eventLoop();
            eventLoop.schedule(() -> client.createBootstrap(eventLoop), 1L, TimeUnit.SECONDS);
        }
    }
}
