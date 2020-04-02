package com.technologysia.mainsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
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
     * 开启线程数为5的异步处理线程池
     */
    private static final ExecutorService workers = Executors.newFixedThreadPool(5);

    private final Selector selector;
    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;
    /**
     * 从反应堆序号
     */
    private final int num;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(2048);
    /**
     * 所有连接完成后都是从一个读取动作开始的
     */
    private int status = READ;

    public AsyncHandler(SocketChannel socketChannel, Selector selector, int num) throws IOException {
        //为了区分Handler被哪个从反应堆触发执行做的标记
        this.num = num;
        //接收客户端连接
        this.socketChannel = socketChannel;
        //置为非阻塞模式
        this.socketChannel.configureBlocking(false);
        //将该客户端注册到selector
        selectionKey = socketChannel.register(selector, 0);
        //附件处理对象
        selectionKey.attach(this);
        //连接已完成，接下来就是读取动作
        selectionKey.interestOps(SelectionKey.OP_READ);
        this.selector = selector;
        this.selector.wakeup();
    }

    @Override
    public void run() {
        //如果一个任务正在异步处理，那么这个run使直接不触发任何处理的，
        //read和send只负责简单的数据读取和响应，业务处理完全不阻塞这里的处理
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
            //重新置为读
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    private void sendWorker() {
        try {
            sendBuffer.clear();
            sendBuffer.put(MessageFormat.format("No{0} SubReactor Received {1} From {2}",
                    num,new String(readBuffer.array()), socketChannel.getRemoteAddress()).getBytes());
            sendBuffer.flip();
            //write方法结束，意味着本次写就绪变为写完成，标记着一次事件的结束
            int count = socketChannel.write(sendBuffer);
            if(count < 0){
                //write场景，取到-1，表示客户端断开连接
                selectionKey.cancel();
                socketChannel.close();
                log.info("No{} SubReactor Send Closed");
            }
            //没断开连接，则再次切换到读
            status = READ;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
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
                //read方法结束，意味着本次读就绪变为读完毕，标记着一次就绪事件的结束
                int count = socketChannel.read(readBuffer);
                if(count > 0){
                    //置为处理中
                    status = PROCESSING;
                    //异步处理
                    workers.execute(this::readWorker);
                }else {
                    selectionKey.cancel();
                    socketChannel.close();
                    log.info("No{} SubReactor Read Closed", num);
                }
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
    }

    /**
     * 读取信息后的业务处理
     */
    private void readWorker() {
        try {
            //模拟耗时操作
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
        try {
            log.info("No{} {} -> Server: {}", num, socketChannel.getRemoteAddress(), new String(readBuffer.array()));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        status = SEND;
        //注册写事件
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        //唤醒阻塞在select的线程
        this.selector.wakeup();
    }
}
