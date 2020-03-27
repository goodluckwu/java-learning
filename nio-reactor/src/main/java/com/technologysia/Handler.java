package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private final SocketChannel socketChannel;
    private final SelectionKey key;
    private ByteBuffer input = ByteBuffer.allocate(1024);
    private ByteBuffer output = ByteBuffer.allocate(1024);
    private static final int READING = 0, SENDING = 1;
    private int state = READING;

    public Handler(Selector selector, SocketChannel serverSocketChannel) throws IOException {
        this.socketChannel = serverSocketChannel;
        this.socketChannel.configureBlocking(false);
        //向selector注册通道
        key = socketChannel.register(selector, 0);
        //可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道
        //将Handler绑定到SelectionKey上
        key.attach( this);
        key.interestOps(SelectionKey.OP_READ);
        //某个线程调用select()方法后阻塞了，即使没有通道已经就绪，也有办法让其从select()方法返回。只要让其它线程在第一个线程调用select()方法的那个对象上调用Selector.wakeup()方法即可。阻塞在select()方法上的线程会立马返回。
        //如果有其它线程调用了wakeup()方法，但当前没有线程阻塞在select()方法上，下个调用select()方法的线程会立即“醒来（wake up）”。
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if(state == READING){
                read();
            }else if(state == SENDING){
                send();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void send() throws IOException {
        socketChannel.write(output);
        if(outputIsComplete()){
            key.cancel();
        }
    }

    private boolean outputIsComplete() {
        return true;
    }

    private void read() throws IOException {
        socketChannel.read(input);
        if(inputIsComplete()){
            process();
            state = SENDING;
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void process() {
    }

    private boolean inputIsComplete() {
        return true;
    }
}
