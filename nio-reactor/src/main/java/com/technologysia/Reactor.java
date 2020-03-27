package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Reactor.class);

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        //注册accept事件
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //给key附加对象，之后可通过attachment获取该对象
        // 调用Acceptor()为回调方法
        selectionKey.attach(new Acceptor());
    }



    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    dispatch(iterator.next());
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey key) {
        //调用SelectionKey绑定的调用对象
        Runnable runnable = (Runnable) key.attachment();
        if(runnable != null){
            runnable.run();
        }
    }

    class Acceptor implements Runnable {

        @Override
        public void run() {
            SocketChannel socketChannel;
            try {
                socketChannel = serverSocketChannel.accept();
                if(socketChannel != null){
                    new Handler(selector, socketChannel);
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }
}
