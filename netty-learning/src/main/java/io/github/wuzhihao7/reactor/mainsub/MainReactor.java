package io.github.wuzhihao7.reactor.mainsub;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Main Reactor：监听客户端连接的反应堆，这里使用jdk并发中的Executors.newSingleThreadExecutor线程池来实现，监听客户端的连接事件(OP_ACCEPT)
 */
public class MainReactor implements Runnable {
    private Selector selector;
    private final SubReactorThreadGroup subReactorThreadGroup;

    public MainReactor(ServerSocketChannel channel){
        try {
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        subReactorThreadGroup = new SubReactorThreadGroup();
    }
    @Override
    public void run() {
        System.out.println("MainReactor is running。。。");
        while (!Thread.interrupted()){
            Set<SelectionKey> ops;
            try {
                selector.select(1000);
                ops = selector.selectedKeys();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            for(Iterator<SelectionKey> it = ops.iterator(); it.hasNext();){
                SelectionKey key = it.next();
                it.remove();
                try {
                    if(key.isAcceptable()){
                        System.out.println("收到客户端的连接请求。。。");
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = ssc.accept();
                        clientChannel.configureBlocking(false);
                        subReactorThreadGroup.dispatch(clientChannel);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("客户端主动断开连接。。。");
                }
            }
        }
    }
}
