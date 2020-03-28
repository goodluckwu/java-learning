package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * reactor模式中Dispatcher类，负责event的分发和eventHandler的维护
 */
public class Dispatcher {
    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * 通过ConcurrentHashMap来维护不同事件处理器
     */
    private Map<EventType, EventHandler> eventHandlerMap = new ConcurrentHashMap<>();

    /**
     * 本例只维护一个selector负责事件选择，netty为了保证性能实现了多个selector来保证循环处理性能，不同事件加入不同的selector的事件缓冲队列
     */
    private final Selector selector;


    public Dispatcher(Selector selector) {
        this.selector = selector;
    }

    /**
     * 在Dispatcher中注册eventHandler
     * @param eventType
     * @param eventHandler
     */
    public void registEventHandler(EventType eventType, EventHandler eventHandler){
        log.info("注册事件");
        eventHandlerMap.put(eventType, eventHandler);
    }

    public void removeEventHandler(EventType eventType){
        eventHandlerMap.remove(eventType);
    }

    public void handleEvents(){
        dispatch();
    }

    /**
     * 此例只是实现了简单的事件分发给相应的处理器处理，例子中的处理器都是同步，在reactor模式的典型实现NIO中都是在handle异步处理，来保证非阻塞
     */
    private void dispatch(){
        log.info("事件分发");
        while (true){
            List<Event> eventList = selector.select();
            for (Event event : eventList) {
                EventHandler eventHandler = eventHandlerMap.get(event.getEventType());
                if(eventHandler != null){
                    eventHandler.handle(event);
                }
            }
        }
    }
}
