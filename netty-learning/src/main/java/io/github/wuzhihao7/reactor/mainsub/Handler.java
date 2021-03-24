package io.github.wuzhihao7.reactor.mainsub;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Handler implements Runnable{
    private static final byte[] b = "hello,服务器收到了你的消息。".getBytes(StandardCharsets.UTF_8);

    private SocketChannel sc;
    private ByteBuffer reqBuffer;
    private SubReactorThread parent;

    public Handler(SocketChannel sc, ByteBuffer reqBuffer, SubReactorThread parent){
        this.sc = sc;
        this.reqBuffer = reqBuffer;
        this.parent = parent;
    }

    @Override
    public void run() {
        System.out.println("业务在handler中开始执行。。。");
        reqBuffer.put(b);
        parent.register(new NioTask(sc, SelectionKey.OP_WRITE, reqBuffer));
        System.out.println("业务在handler中执行结束。。。");
    }
}
