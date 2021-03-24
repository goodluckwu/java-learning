package io.github.wuzhihao7.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ChanClient {
    private SocketChannel socketChannel;
    private Selector selector;

    public ChanClient(){
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            handleConnect();
            handleReponse();
            handleRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleReponse() {
        new Thread(() -> {
            while (true){
                try {
                    int select = selector.select();
                    if(select > 0){
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()){
                            SelectionKey selectionKey = iterator.next();
                            if(selectionKey.isReadable()){
                                SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                                ByteBuffer buffer = ByteBuffer.allocate(10);
                                StringBuilder stringBuilder = new StringBuilder();
                                int len=0;
                                while((len=socketChannel.read(buffer))>0){
                                    buffer.flip();
                                    stringBuilder.append(new String(buffer.array(), 0 ,len));
                                    buffer.clear();
                                }
                                System.out.println(stringBuilder.toString());
                            }
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void handleRequest() {
        ByteBuffer buf=ByteBuffer.allocate(1024);
        Scanner scanner =new Scanner(System.in);
        while(scanner.hasNext()){
            String inputStr=scanner.next();
            buf.put(inputStr.getBytes());
            buf.flip();
            try {
                while (buf.hasRemaining()) {
                    socketChannel.write(buf);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            buf.clear();
        }
        scanner.close();
    }

    private void handleConnect() throws IOException {
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666));
        int select = selector.select();
        if(select > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isConnectable()){
                    //连接建立或者连接建立不成功
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //完成连接的建立
                    /**
                     * 首先，在non-blocking模式下调用socketChannel.connect(new InetSocketAddress("127.0.0.1",8080));
                     * 连接远程主机，如果连接能立即建立就像本地连接一样，该方法会立即返回true，否则该方法会立即返回false,然后系统底层进行三次握手建立连接。
                     * 连接有两种结果，一种是成功连接，第二种是异常，但是connect方法已经返回，无法通过该方法的返回值或者是异常来通知用户程序建立连接的情况，
                     * 所以由OP_CONNECT事件和finishConnect方法来通知用户程序。
                     * 不管系统底层三次连接是否成功，selector都会被唤醒继而触发OP_CONNECT事件，如果握手成功，并且该连接未被其他线程关闭，finishConnect会返回true，
                     * 然后就可以顺利的进行channle读写。如果网络故障，或者远程主机故障，握手不成功，用户程序可以通过finishConnect方法获得底层的异常通知，进而处理异常。
                     */
                    if(channel.finishConnect()) {
                        System.out.println("客户端连接成功");
                    }else {
                        System.out.println("客户端连接失败");
                    }
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) {
        new ChanClient().start();
    }
}
