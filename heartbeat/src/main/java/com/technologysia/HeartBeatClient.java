package com.technologysia;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HeartBeatClient {
    private static final Logger log = LoggerFactory.getLogger(HeartBeatClient.class);

    private final int port;
    private final String host;

    public HeartBeatClient(int port, String host){
        this.port = port;
        this.host = host;
    }

    public void start() {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class);
            HeartBeatClientHandler heartBeatClientHandler = new HeartBeatClientHandler(this);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new IdleStateHandler(1, 0, 0));
                    pipeline.addLast(heartBeatClientHandler);
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.cause() != null) {
                        f.channel().eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                log.info("Reconnecting to: [{}:{}]", host, port);
                                start();
                            }
                        }, 5, TimeUnit.SECONDS);
                    }
                }
            });
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new HeartBeatClient(8899,"localhost").start();
    }
}
