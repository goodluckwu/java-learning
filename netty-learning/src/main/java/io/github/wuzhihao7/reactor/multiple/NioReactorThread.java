package io.github.wuzhihao7.reactor.multiple;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class NioReactorThread extends Thread {
    private static final byte[] b = "hello,服务器收到了你的消息".getBytes(StandardCharsets.UTF_8);

    private Selector selector;
    private final List<SocketChannel> waitRegisterList = new ArrayList<>(512);
    private ReentrantLock registerLock = new ReentrantLock();

    public NioReactorThread() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(SocketChannel socketChannel) {
        if (socketChannel != null) {
            try {
                registerLock.lock();
                waitRegisterList.add(socketChannel);
            } finally {
                registerLock.unlock();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Set<SelectionKey> ops;
                try {
                    selector.select(1000);
                    ops = selector.selectedKeys();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                for (Iterator<SelectionKey> it = ops.iterator(); it.hasNext(); ) {
                    SelectionKey key = it.next();
                    it.remove();

                    try {
                        if (key.isWritable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            ByteBuffer buf = (ByteBuffer) key.attachment();
                            buf.flip();
                            clientChannel.write(buf);
                            System.out.println("服务端向客户端发送数据。。。");
                            clientChannel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            System.out.println("服务端接收客户端连接请求。。。");
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            ByteBuffer buf = ByteBuffer.allocate(1024);
                            clientChannel.read(buf);
                            buf.put(b);
                            clientChannel.register(selector, SelectionKey.OP_WRITE, buf);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        System.out.println("客户端主动断开连接。。。");
                        key.channel().close();
                        key.cancel();
                    }
                }

                if (!waitRegisterList.isEmpty()) {
                    try {
                        registerLock.lock();
                        for (Iterator<SocketChannel> it = waitRegisterList.iterator(); it.hasNext(); ) {
                            SocketChannel sc = it.next();
                            try {
                                sc.configureBlocking(false);
                                sc.register(selector, SelectionKey.OP_READ);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            it.remove();
                        }
                    } finally {
                        registerLock.unlock();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
