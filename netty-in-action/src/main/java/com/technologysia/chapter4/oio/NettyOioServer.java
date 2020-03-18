package com.technologysia.chapter4.oio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.oio.OioEventLoopGroup;

import java.nio.charset.StandardCharsets;

public class NettyOioServer {
    public void serve(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hello, World!", StandardCharsets.UTF_8));
        OioEventLoopGroup group = new OioEventLoopGroup();
        try {
            //创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
