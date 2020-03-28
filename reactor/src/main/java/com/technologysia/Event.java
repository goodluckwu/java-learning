package com.technologysia;

/**
 * reactor模式的事件类，可以理解为将输入原始对象根据不同状态包装成一个事件类，reactor模式里处理的都是event事件对象
 */
public class Event {
    private InputSource source;
    private EventType eventType;

    public InputSource getSource() {
        return source;
    }

    public void setSource(InputSource source) {
        this.source = source;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
