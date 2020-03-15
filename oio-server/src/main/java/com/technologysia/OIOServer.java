package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OIOServer {
    private static final Logger log = LoggerFactory.getLogger(OIOServer.class);
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
                                int len;
                                //按字节流方式读取数据
                                while((len = inputStream.read(data)) != -1){
                                    log.info("recv-[{}]", new String(data, 0, len));
                                }
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
        log.info("server has started at listening port 8080");
    }
}
