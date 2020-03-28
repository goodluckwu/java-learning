package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * reactor模式中的Demultiplexer类，提供select（）方法用于从缓冲队列中查找出符合条件的event列表
 */
public class Selector {
    private static final Logger log = LoggerFactory.getLogger(Selector.class);
    /**
     * 定义一个链表阻塞队列实现缓冲队列，用于保证线程安全
     */
    private BlockingDeque<Event> eventQueue = new LinkedBlockingDeque<>();

    /**
     * 定义一个object用于synchronized方法加锁
     */
    private final Object lock = new Object();

    public List<Event> select(){
        return select(0);
    }

    public List<Event> select(long timeout){
        if(timeout > 0){
            if(eventQueue.isEmpty()){
                synchronized (lock){
                    if(eventQueue.isEmpty()){
                        try {
                            lock.wait(timeout);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }
        List<Event> events = new ArrayList<>();
        //例子中只是简单的将event列表全部返回，可以在此处增加业务逻辑，选出符合条件的event进行返回
        eventQueue.drainTo(events);
        return events;
    }

    public void addEvent(Event e){
        boolean success = eventQueue.offer(e);
        if(success){
            synchronized (lock){
                //如果有新增事件则对lock对象解锁
                lock.notify();
            }
        }
    }
}
