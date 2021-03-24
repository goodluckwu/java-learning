package io.github.wuzhihao7.reactor.multiple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) {
        new Thread(new Acceptor()).start();
    }

    private static final class Acceptor implements Runnable{
        private NioReactorThreadGroup nioReactorThreadGroup;

        public Acceptor(){
            nioReactorThreadGroup = new NioReactorThreadGroup();
        }


        @Override
        public void run() {
            System.out.println("服务端启动成功，等待客户端接入");
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

                    for(Iterator<SelectionKey> it = ops.iterator();it.hasNext();){
                        SelectionKey key = it.next();
                        it.remove();

                        try{
                            if(key.isAcceptable()){
                                System.out.println("收到客户端的连接请求。。。");
                                SocketChannel clientChannel = ssc.accept();
                                nioReactorThreadGroup.dispath(clientChannel);
                            }
                        }catch (Throwable e){
                            e.printStackTrace();
                            System.out.println("客户端主动断开连接。。。");
                            key.channel().close();
                            key.channel();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
