package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * reactor的事件接收类，负责初始化selector和接收缓冲队列
 */
public class Acceptor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Acceptor.class);

    private final int port;
    private final Selector selector;

    /**
     * 代表 serversocket，通过LinkedBlockingQueue来模拟外部输入请求队列
     */
    private BlockingDeque<InputSource> sourceQueue = new LinkedBlockingDeque<>();

    public Acceptor(int port, Selector selector) {
        this.port = port;
        this.selector = selector;
    }

    /**
     * 外部有输入请求后，需要加入到请求队列中
     * @param source
     */
    public void addNewConnection(InputSource source){
        sourceQueue.offer(source);
    }

    public int getPort(){
        return this.port;
    }

    @Override
    public void run() {
        log.info("事件接收器开始工作");
        while (true){
            InputSource source = null;
            try {
                //相当于 serversocket.accept()，接收输入请求，该例从请求队列中获取输入请求
                source = sourceQueue.take();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
            if(source != null){
                Event acceptEvent = new Event();
                acceptEvent.setSource(source);
                acceptEvent.setEventType(EventType.ACCEPT);
                selector.addEvent(acceptEvent);
            }
        }
    }
}
