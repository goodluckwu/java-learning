package com.technologysia.mainsub;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

public class MainReactor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MainReactor.class);

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public MainReactor(int port) throws IOException {
        //打开一个selector
        selector = Selector.open();
        //建立一个server端通道
        serverSocketChannel = ServerSocketChannel.open();
        //绑定服务端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        //selector模式下，所有通道都必须是非阻塞的
        serverSocketChannel.configureBlocking(false);
        //MainReactor是入口，最初给一个channel注册上去的事件都是accept
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //绑定Acceptor处理类
        selectionKey.attach(new Acceptor(serverSocketChannel));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                int count = selector.select();
                if(count == 0){
                    continue;
                }
                //拿到本次select获取的就绪事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    dispatch(selectionKey);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey selectionKey) {
        //附带对象为Acceptor
        Runnable r = (Runnable) selectionKey.attachment();
        //调用之前附加的回调对象
        if(r != null){
            r.run();
        }
    }
}
