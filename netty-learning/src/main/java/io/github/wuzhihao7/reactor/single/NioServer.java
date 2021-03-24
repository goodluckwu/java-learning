package io.github.wuzhihao7.reactor.single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) {
        new Thread(new Reactor()).start();
    }

    private static final class Reactor implements Runnable {
        private static final byte[] b = "hello,服务器收到了你的消息".getBytes(StandardCharsets.UTF_8);

        @Override
        public void run() {
            System.out.println("服务器启动成功，等待客户端接入");
            ServerSocketChannel ssc;
            Selector selector;
            try {
                ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                ssc.bind(new InetSocketAddress("127.0.0.1", 9080));

                selector = Selector.open();
                ssc.register(selector, SelectionKey.OP_ACCEPT);

                Set<SelectionKey> ops;
                while (true){
                    try {
                        selector.select();
                        ops = selector.selectedKeys();
                    }catch (Throwable e){
                        e.printStackTrace();
                        break;
                    }

                    for(Iterator<SelectionKey> it = ops.iterator(); it.hasNext();){
                        SelectionKey key = it.next();
                        it.remove();

                        try {
                            if(key.isAcceptable()){
                                SocketChannel clientChannel = ssc.accept();
                                clientChannel.configureBlocking(false);
                                clientChannel.register(selector,SelectionKey.OP_READ);
                                System.out.println("收到客户端的连接请求。。。");
                            }else if(key.isWritable()){
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                ByteBuffer buf = (ByteBuffer) key.attachment();
                                buf.flip();
                                clientChannel.write(buf);
                                System.out.println("服务端向客户端发送数据。。。");
                                clientChannel.register(selector, SelectionKey.OP_READ);
                            }else if(key.isReadable()){
                                System.out.println("服务端接收客户端连接请求。。。");
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                ByteBuffer buf = ByteBuffer.allocate(1024);
                                clientChannel.read(buf);
                                buf.put(b);
                                clientChannel.register(selector,SelectionKey.OP_WRITE, buf);
                            }
                        }catch (Throwable e){
                            e.printStackTrace();
                            System.out.println("客户端主动断开连接。。。");
                            key.channel().close();
                            key.cancel();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
