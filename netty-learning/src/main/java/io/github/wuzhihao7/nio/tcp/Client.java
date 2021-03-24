package io.github.wuzhihao7.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws Exception {
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch end = new CountDownLatch(5);
        ExecutorService exce = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            Runnable run = () -> {
                try {
                    start.await();
                    // 测试执行具体的方法
                    SocketChannel socketChannel = SocketChannel.open();
                    socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
                    socketChannel.write(ByteBuffer.wrap(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));

                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                } finally {
                    end.countDown();
                }
            };
            exce.submit(run);
        }
        start.countDown();
        end.await();
        exce.shutdown();
        while (!exce.awaitTermination(1, TimeUnit.SECONDS)){
            System.out.println("等待并发结束");
        }
    }
}
