package com.technologysia.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AsyncHandler.class);
    /**
     * 读取就绪
     */
    private static final int READ = 0;
    /**
     * 响应就绪
     */
    private static final int SEND = 1;
    /**
     * 处理中
     */
    private static final int PROCESSING = 2;
    /**
     * 开启线程数为4的异步处理线程池
     */
    private static final ExecutorService workers = Executors.newFixedThreadPool(4);

    private final Selector selector;
    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(2048);

    /**
     * 所有连接完成后都是从一个读取动作开始的
     */
    private int status = READ;

    public AsyncHandler(SocketChannel socketChannel, Selector selector) throws IOException {
        //接收客户端连接
        this.socketChannel = socketChannel;
        //置为非阻塞模式
        this.socketChannel.configureBlocking(false);
        //将该客户端注册到selector
        selectionKey = socketChannel.register(selector, 0);
        //附加处理对象，当前是Handlerd对象
        selectionKey.attach(this);
        //连接已经完成，接下来就是读取操作
        selectionKey.interestOps(SelectionKey.OP_READ);
        this.selector = selector;
        this.selector.wakeup();
    }

    @Override
    public void run() {
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
    }

    private void send() {
        if(selectionKey.isValid()){
            //置为执行中
            status = PROCESSING;
            //异步处理
            workers.execute(this::sendWorker);
            //重新设置为读
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    private void sendWorker() {
        try {
            sendBuffer.clear();
            sendBuffer.put(("Received" + new String(readBuffer.array()) + " from " + socketChannel.getRemoteAddress()).getBytes());
            sendBuffer.flip();

            //write方法结束，意味着本次写就绪变为写完毕，标识着一次事件的结束
            int count = socketChannel.write(sendBuffer);
            if (count < 0) {
                //write场景下，取到-1，意味着客户端断开连接
                selectionKey.cancel();
                socketChannel.close();
                log.warn("Send Closed");
            }

            //没断开连接，则再次切换到读
            status = READ;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException ex) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void read() {
        if(selectionKey.isValid()){
            try {
                readBuffer.clear();
                //read方法结束，意味着本次“读就绪”变为“读完毕”，标识着一次就绪事件的结束
                int count = socketChannel.read(readBuffer);
                if(count > 0){
                    //置为处理中
                    status = PROCESSING;
                    //异步处理
                    workers.execute(this::readWorker);
                }else {
                    //读模式拿到的值是-1，说明客户端已经d断开连接，那么将对应的selectKey从selector里清除，
                    //否则下次还会select到，因为断开连接意味着读就绪不会变成读完毕，也不cancel，
                    //下次select会不停的收到该事件
                    //所以在这种场景下，需要关闭socketChannel并且取消key，最好是退出当前函数
                    //注意，这个时候服务端要是继续使用该socketChannelj进行读操作的话，
                    //就会抛出“远程主机强迫关闭一个现有的连接”的IO异常
                    selectionKey.cancel();
                    socketChannel.close();
                    log.warn("Read Closed");
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                selectionKey.cancel();
                try {
                    socketChannel.close();
                } catch (IOException ex) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    /**
     * 读入信息后的业务处理
     */
    private void readWorker() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("client -> server: {}", new String(readBuffer.array()));
        status = SEND;
        //注册写事件
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        //唤醒阻塞在select上的线程
        this.selector.wakeup();
    }

}
