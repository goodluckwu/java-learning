package com.technologysia;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    public Bootstrap createBootstrap(EventLoopGroup eventLoopGroup){
        ClientHandler clientHandler = new ClientHandler(this);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(clientHandler);
                    }
                });
        bootstrap.connect(this.host, this.port).addListener(new ConnectionListener(this));
        return bootstrap;
    }

    public void start(){
        createBootstrap(new NioEventLoopGroup());
    }

    public static void main(String[] args) {
        new Client("localhost", 8899).start();
    }
}
