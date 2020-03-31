package com.technologysia.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

public class Reactor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Reactor.class);

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    /**
     * Reactor初始化
     * @param port 服务端监听端口号
     * @throws IOException
     */
    public Reactor(int port) throws IOException {
        //打开一个Selector
        selector = Selector.open();
        //建立一个server端通道
        serverSocketChannel = ServerSocketChannel.open();
        //绑定服务端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        //selector模式下，所有通道必须是非阻塞的
        serverSocketChannel.configureBlocking(false);

        //Reactor是入口，最初给一个channel注册上去的事件都是accept
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //附加回调对象Acceptor
        selectionKey.attach(new Acceptor(selector, serverSocketChannel));
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                //就绪事件到达之前，阻塞
                selector.select();
                //拿到本次select获取的就绪事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    //任务分发
                    dispatch(selectionKey);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void dispatch(SelectionKey selectionKey) {
        Runnable runnable = (Runnable) selectionKey.attachment();
        if(runnable != null){
            long start = System.nanoTime();
            runnable.run();
            long end = System.nanoTime();
            log.info("[Cost Time] - {}ns", end - start);
        }
    }
}
