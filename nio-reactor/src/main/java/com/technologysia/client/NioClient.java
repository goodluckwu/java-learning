package com.technologysia.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class NioClient implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(NioClient.class);

    private SocketChannel socketChannel;
    private Selector selector;

    public NioClient(String host, int port) throws IOException {
        //打开一个selector
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        //设置未非阻塞模式
        socketChannel.configureBlocking(false);
        //连接服务
        socketChannel.connect(new InetSocketAddress(host, port));

        //入口，最初给一个客户端channel注册上去的事件是连接事件
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);

        //附件处理类，第一次初始化放的类就是连接就绪处理类
        selectionKey.attach(new Connector(selector, socketChannel));
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                //就绪事件到达，阻塞
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
        //附带对象为Connector
        Runnable r = (Runnable) selectionKey.attachment();

        //调用之前注册的回调对象
        if(r != null){
            r.run();
        }
    }
}
