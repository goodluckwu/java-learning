package com.technologysia.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class Handler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private static final int READ = 0;
    private static final int SEND = 1;

    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;

    private ByteBuffer readBuffer = ByteBuffer.allocate(2048);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
    private int status = READ;
    private AtomicInteger counter = new AtomicInteger();

    public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
        //接收客户端连接
        this.socketChannel = socketChannel;
        //置为非阻塞模式
        this.socketChannel.configureBlocking(false);
        //将该客户端注册到selector
        selectionKey = this.socketChannel.register(selector, 0);
        //附件处理对象，当前是Handler对象
        selectionKey.attach(this);
        //建立连接完成，接下来是写操作
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        //唤起select阻塞
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            switch (status){
                case READ:
                    read();
                    break;
                case SEND:
                    send();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void send() throws IOException {
        if(selectionKey.isValid()){
            sendBuffer.clear();
            int count = counter.incrementAndGet();
            if(count <= 10){
                sendBuffer.put(MessageFormat.format("msg is {0}",count).getBytes());
                //切到读模式，用于让通道读取buffer中的数据
                sendBuffer.flip();
                socketChannel.write(sendBuffer);

                //再次切换到读，用于接收服务端响应
                status = READ;
                selectionKey.interestOps(SelectionKey.OP_READ);
            }else {
                selectionKey.cancel();
                socketChannel.close();
            }
        }
    }

    private void read() throws IOException {
        if(selectionKey.isValid()){
            readBuffer.clear();
            //切换成buffer的写模式，用于让通道将自己的内容写入到buffer里
            socketChannel.read(readBuffer);
            log.info("Server -> Client: [{}]", new String(readBuffer.array()));

            //收到服务端响应后，再继续往服务端发送数据
            status = SEND;
            //注册写事件
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }
}
