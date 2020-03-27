package com.technologysia;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServer {
    private static final Logger log = LoggerFactory.getLogger(EchoServer.class);

    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public void start() throws Exception {
        log.info("Echo Server Starting...");
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ChannelOutboundHandler 在注册的时候需要放在最后一个ChannelInboundHandler之前, 否则将无法传递到ChannelOutboundHandler
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new EchoOutBoundHandler1());
                            pipeline.addLast(new EchoOutBoundHandler2());
                            pipeline.addLast(new EchoInBoundHandler1());
                            pipeline.addLast(new EchoInBoundHandler2());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture f = b.bind(port).sync();
            log.info("Echo Server Started At Listening - [{}]", port);
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(1234).start();
    }
}
