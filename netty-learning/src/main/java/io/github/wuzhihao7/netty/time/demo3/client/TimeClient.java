package io.github.wuzhihao7.netty.time.demo3.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TimeClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new TimeClientHandler());
                        }
                    });

            CountDownLatch countDownLatch = new CountDownLatch(1);
            ExecutorService executorService = Executors.newCachedThreadPool();

            IntStream.range(0,10).forEach(i -> {
                executorService.execute(() -> {
                    try {
                        countDownLatch.await();
                        ChannelFuture f = b.connect("127.0.0.1", 8080).sync();
                        f.channel().closeFuture().sync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
            countDownLatch.countDown();
            executorService.shutdown();
            while (!executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)){
                System.out.println("线程还在执行。。。");
            }

        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
