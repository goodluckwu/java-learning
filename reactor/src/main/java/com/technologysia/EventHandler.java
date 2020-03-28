package com.technologysia;

/**
 * 处理事件的抽象类，里面包含了不同事件处理器的公共逻辑和公共对象
 */
public abstract class EventHandler {
    private InputSource source;

    public abstract void handle(Event event);

    public InputSource getSource() {
        return source;
    }

    public void setSource(InputSource source) {
        this.source = source;
    }
}
