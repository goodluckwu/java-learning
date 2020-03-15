package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class OIOClient {
    private static final Logger log = LoggerFactory.getLogger(OIOClient.class);
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 8080);
                while(true){
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((LocalDateTime.now() + ", hello world!").getBytes());
                    outputStream.flush();
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (IOException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }).start();
    }
}
