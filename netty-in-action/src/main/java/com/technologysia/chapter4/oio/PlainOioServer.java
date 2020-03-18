package com.technologysia.chapter4.oio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PlainOioServer {
    private static final Logger log = LoggerFactory.getLogger(PlainOioServer.class);

    public void serve(int port) throws IOException {
        //将服务器绑定到指定端口
        final ServerSocket serverSocket = new ServerSocket(port);
            for(;;){
                //接受连接
                Socket socket = serverSocket.accept();
                log.info("Accepted connection from {}", socket);
                //创建一个新的线程来处理该连接
                new Thread(() -> {
                    OutputStream out;
                    try {
                        out = socket.getOutputStream();
                        //将消息写给已连接的客户端
                        out.write("Hello, World!\r\n".getBytes(StandardCharsets.UTF_8));
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            //关闭连接
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
    }

    public static void main(String[] args) throws IOException {
        new PlainOioServer().serve(8080);
    }
}
