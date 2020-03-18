package com.technologysia.chapter4.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {
    private static final Logger log = LoggerFactory.getLogger(PlainNioServer.class);

    public void serve(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        //将服务器绑定到选定的端口
        serverSocket.bind(address);
        //打开Selector来处理Channel
        Selector selector = Selector.open();
        //将ServerSocket注册到Selector以接受连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hello, World!\r\n".getBytes(StandardCharsets.UTF_8));
        for(;;){
            //等待需要处理的新事件；阻塞将一直持续到下一个传入事件
            selector.select();
            //获取所有接收事件的SelectionKey实例
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                //检查事件是否是一个新的已经就绪可以被接受的连接
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    //接受客户端，并将它注册到选择器
                    client.register(selector, SelectionKey.OP_READ| SelectionKey.OP_WRITE, msg.duplicate());
                    log.info("Accepted connection from " + client);
                }else if(key.isWritable()){
                    //检查套接子是否已经准备好写数据
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    //将数据写到已经连接的客户端
                    while (buffer.hasRemaining()){
                        if(clientChannel.write(buffer) == 0){
                            break;
                        }
                    }
                    clientChannel.close();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new PlainNioServer().serve(8080);
    }
}
