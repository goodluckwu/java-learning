package com.technologysia.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Connector implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Connector.class);

    private final Selector selector;
    private final SocketChannel socketChannel;

    public Connector(Selector selector, SocketChannel socketChannel){
        this.selector = selector;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            if(socketChannel.finishConnect()){
                //连接完成（与服务端三次握手完成）
                log.info("Connect To [{}]", socketChannel.getRemoteAddress());

                //连接建立完成后，接下来的动作交给Handler去处理（读写等）
                new Handler(socketChannel, selector);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
