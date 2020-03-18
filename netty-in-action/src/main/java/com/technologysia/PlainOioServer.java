package com.technologysia;

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
        final ServerSocket serverSocket = new ServerSocket(port);
            for(;;){
                Socket socket = serverSocket.accept();
                log.info("Accepted connection from {}", socket);
                new Thread(() -> {
                    OutputStream out;
                    try {
                        out = socket.getOutputStream();
                        out.write("Hello, World!\r\n".getBytes(StandardCharsets.UTF_8));
                        out.flush();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
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
