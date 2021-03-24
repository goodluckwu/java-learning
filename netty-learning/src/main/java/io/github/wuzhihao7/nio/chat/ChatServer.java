package io.github.wuzhihao7.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class ChatServer {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public ChatServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        try {
            serverSocketChannel.bind(new InetSocketAddress(6666));
            System.out.println("ChatServer started at listening 6666");
            while (true) {
                handleConnect();
                //处理请求
                handleRequest();
                //响应请求
                handleResponse();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnect() throws IOException {
        int select = selector.select();
        if (select > 0) {
            //有连接进来
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println(socketChannel.getRemoteAddress().toString() + "上线");
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }
    }

    public void handleRequest() throws IOException {
        int select = selector.select();
        if (select > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    StringBuilder stringBuilder = new StringBuilder();
                    while (true) {
                        buffer.clear();
                        int read = socketChannel.read(buffer);
                        if (read == 0) {
                            System.out.println("服务器收到来自[" + socketChannel.getRemoteAddress() + "]消息：" + stringBuilder.toString());
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_WRITE, stringBuilder.toString());
                            break;
                        } else if (read == -1) {
                            System.out.println("客户端已下线");
                            selectionKey.cancel();
                            socketChannel.close();
                            break;
                        }
                        buffer.flip();
                        stringBuilder.append(new String(buffer.array(), 0, read));
                    }
                }
                iterator.remove();
            }
        }
    }

    private void handleResponse() throws IOException {
        int select = selector.select();
        if (select > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    String message = (String) selectionKey.attachment();
                    System.out.println("服务端响应消息：" + message);
                    for (SelectionKey key : selector.keys()) {
                        SelectableChannel channel = key.channel();
                        if (channel instanceof SocketChannel) {
                            SocketChannel socketChannel1 = (SocketChannel) channel;
                            socketChannel1.write(ByteBuffer.wrap(message.getBytes()));
                        }
                    }
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
