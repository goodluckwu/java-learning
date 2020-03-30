package com.technologysia;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class UptimeClient {
    static final String HOST = System.getProperty("host", "localhost");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    static final int RECONNECT_DELAY = Integer.parseInt(System.getProperty("reconnectDelay", "5"));
    private static final int READ_TIMEOUT = Integer.parseInt(System.getProperty("readTimeout", "10"));

    private static final UptimeClientHandler HANDLER = new UptimeClientHandler();
    private static final Bootstrap b = new Bootstrap();

    public static void main(String[] args) {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        b.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(HOST, PORT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new IdleStateHandler(READ_TIMEOUT, 0, 0), HANDLER);
                    }
                });

        b.connect();
    }

    static void connect(){
        b.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.cause() != null){
                    HANDLER.startTime = -1;
                    HANDLER.println("Failed to connect: " + future.cause());
                }
            }
        });
    }

}
