package com.technologysia.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class Handler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private final static int READ = 0;
    private final static int SEND = 1;

    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(2048);
    private int status = READ;

    public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
        //接收客户端连接
        this.socketChannel = socketChannel;
        //置为非阻塞模式
        this.socketChannel.configureBlocking(false);
        //将该客户端注册到selector
        selectionKey = socketChannel.register(selector, 0);
        //附加处理对象，当前是Handler对象
        selectionKey.attach(this);
        //连接已完成，那么接下来就是读取动作
        selectionKey.interestOps(SelectionKey.OP_READ);
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
            //这里的异常处理是做了汇总，常出的异常就是server端还有未读/写完的客户端消息，
            //客户端就主动断开连接，这种情况下是不会触发返回-1的，
            //这样下面read和write方法里的cancel和close就无法触发，会导致死循环异常
            //（read和write处理失败，事件又未被cancel，因此会不断的被select到，不断的报异常）
            log.error(e.getMessage(), e);
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException ex) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void send() throws IOException {
        if(selectionKey.isValid()){
            sendBuffer.clear();
            sendBuffer.put(MessageFormat.format("Recvived {0} From {1}", new String(readBuffer.array()), socketChannel.getRemoteAddress()).getBytes());
            sendBuffer.flip();

            //write方法结束，意味着本次写就绪变为写完毕，标识着一次事件的结束
            int count = socketChannel.write(sendBuffer);

            if(count < 0){
                //同理，write场景下，取到-1，也意味着客户端断开连接
                selectionKey.cancel();
                socketChannel.close();
                log.info("Send Closed");
            }

            //没断开连接，则再次切换到读
            status = READ;
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    private void read() throws IOException {
        if(selectionKey.isValid()){
            log.info("服务端读取数据前");
            readBuffer.clear();

            //read方法结束，意味着本次”读就绪”变为“读完毕”，标记着一次就绪事件的结束
            int count = socketChannel.read(readBuffer);
            if(count > 0){
                try {
                    //读取消息后睡眠5秒，模拟业务处理瓶颈
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }

                log.info("Recvived [{}] From [{}]", new String(readBuffer.array()), socketChannel.getRemoteAddress());
                status = SEND;
                //注册写事件
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            }else {
                //读模式下拿到的值是-1，说明客户端已经断开连接，那么将对应的selectionKey从selector里清除
                //否则下次还会select到，因为断开连接意味着读就绪不会变成读完毕，也不cancel，
                //下次select会不停收到该事件。
                //所以在这种场景下，（服务器程序）你需要关闭socketChannel并且取消key，最好退出当前函数。
                //注意，这个时候服务端要是继续使用该socketChannel进行读操作的话，就会抛出“远程主机强迫关闭一个现有的连接”的IO异常。
                selectionKey.cancel();
                socketChannel.close();
                log.info("Read Closed");
            }
        }
    }
}
