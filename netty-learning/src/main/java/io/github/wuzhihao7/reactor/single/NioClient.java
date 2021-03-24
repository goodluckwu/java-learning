package io.github.wuzhihao7.reactor.single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioClient {
    public static void main(String[] args) {
        SocketChannel clientChannel;
        Selector selector;
        try {
            clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false);

            selector = Selector.open();
            clientChannel.register(selector,SelectionKey.OP_CONNECT);

            clientChannel.connect(new InetSocketAddress("127.0.0.1", 9080));

            Set<SelectionKey> ops;

            while (true){
                try {
                    selector.select();
                    ops = selector.selectedKeys();
                    for(Iterator<SelectionKey> it = ops.iterator();it.hasNext();){
                        SelectionKey key = it.next();
                        it.remove();
                        try {
                            if(key.isConnectable()){
                                System.out.println("客户端连接");
                                //判断此通道上是否正在进行连接操作。
                                //完成套接字通道的连接过程
                                if(clientChannel.isConnectionPending()){
                                    clientChannel.finishConnect();
                                    System.out.println("完成连接");
                                    clientChannel.register(selector,SelectionKey.OP_WRITE);
                                }
                            }else if(key.isWritable()){
                                System.out.println("客户端写");
                                ByteBuffer buf = ByteBuffer.allocate(1024);
                                buf.put("Hello,Server.".getBytes(StandardCharsets.UTF_8));
                                buf.flip();
                                clientChannel.write(buf);
                                clientChannel.register(selector,SelectionKey.OP_READ);
                            }else if(key.isReadable()){
                                System.out.println("客户端收到服务器的响应");
                                ByteBuffer buf = ByteBuffer.allocate(1024);
                                int count = clientChannel.read(buf);
                                if(count > 0){
                                    buf.flip();
                                    System.out.println(new String(buf.array()));
                                }
                            }
                        }catch (Throwable e){
                            System.out.println("服务端断开连接。。。");
                            e.printStackTrace();
                        }
                    }
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
