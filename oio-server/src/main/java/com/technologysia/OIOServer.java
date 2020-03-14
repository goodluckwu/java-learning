package com.technologysia;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        //接收新连接线程
        new Thread(() -> {
            while (true){
                try {
                    //阻塞方法获取新的连接
                    Socket socket = serverSocket.accept();
                    //每个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {
                        byte[] data = new byte[1024];
                        try {
                            InputStream inputStream = socket.getInputStream();
                            while (true){

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
