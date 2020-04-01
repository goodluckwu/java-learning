package com.technologysia.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Acceptor.class);

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public Acceptor(Selector selector, ServerSocketChannel serverSocketChannel){
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            if(socketChannel != null){
                log.info("Accept From [{}]", socketChannel.getRemoteAddress());
                //把客户端通道传给Handler，Handler负责接下来的事件处理
                new AsyncHandler(socketChannel,selector);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
