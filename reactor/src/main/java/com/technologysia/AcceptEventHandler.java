package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ACCEPT事件处理器
 */
public class AcceptEventHandler extends EventHandler {
    private static final Logger log = LoggerFactory.getLogger(AcceptEventHandler.class);

    private final Selector selector;

    public AcceptEventHandler(Selector selector) {
        this.selector = selector;
    }


    @Override
    public void handle(Event event) {
        //处理ACCEPT的event事件
        if(event.getEventType() == EventType.ACCEPT){
            log.info("接受请求-[{}]", event.getSource());
            //处理ACCEPT状态事件
            //将事件状态改为下一个READ状态，并放入selector的缓冲队列中
            Event readEvent = new Event();
            readEvent.setSource(event.getSource());
            readEvent.setEventType(EventType.READ);
            selector.addEvent(readEvent);
        }
    }
}
