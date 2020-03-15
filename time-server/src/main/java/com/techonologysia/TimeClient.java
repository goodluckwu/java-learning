package com.techonologysia;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeClient {
    private static final Logger log = LoggerFactory.getLogger(TimeClient.class);

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 37;

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //Bootstrap is similar to ServerBootstrap except that
            // it's for non-server channels such as a client-side or connectionless channel.
            Bootstrap b = new Bootstrap();
            //If you specify only one EventLoopGroup,
            // it will be used both as a boss group and as a worker group.
            // The boss worker is not used for the client side though.
            b.group(workerGroup)
                    //Instead of NioServerSocketChannel, NioSocketChannel is being used to create a client-side Channel.
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new TimeClientHandler());
//                            ch.pipeline().addLast(new TimeClientHandler2());
//                            ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                            ch.pipeline().addLast(new TimeDecoder2(), new TimeClientHandler());
                        }
                    })
                    //Note that we do not use childOption() here unlike we did with ServerBootstrap because the client-side SocketChannel does not have a parent.
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
