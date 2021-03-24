package io.github.wuzhihao7.reactor.mainsub;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

public class NioTask implements Serializable {
    private SocketChannel sc;
    private int op;
    private Object data;

    public NioTask(SocketChannel sc, int op){
        this(sc, op, null);
    }

    public NioTask(SocketChannel sc, int op, Object data) {
        this.sc = sc;
        this.op = op;
        this.data = data;
    }

    public SocketChannel getSc() {
        return sc;
    }

    public void setSc(SocketChannel sc) {
        this.sc = sc;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
