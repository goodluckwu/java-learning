package io.github.wuzhihao7.reactor.mainsub;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServer {
    private static final int DEFAULT_PORT = 9080;

    public static void main(String[] args) {
        new Thread(new Acceptor()).start();
    }

    /**
     * Acceeptor：职责维护java.nio.ServerSocketChannel类，绑定服务端监听端口，然后将该通道注册到MainRector中
     */
    public static class Acceptor implements Runnable{

        private static final ExecutorService mainReactor = Executors.newSingleThreadExecutor();

        @Override
        public void run() {
            ServerSocketChannel ssc;
            try {
                ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                ssc.bind(new InetSocketAddress("127.0.0.1", DEFAULT_PORT));

                dispatch(ssc);

                System.out.println("服务端启动成功。。。");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void dispatch(ServerSocketChannel ssc) {
            mainReactor.execute(new MainReactor(ssc));
        }
    }
}
