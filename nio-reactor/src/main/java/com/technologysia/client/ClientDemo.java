package com.technologysia.client;

import java.io.IOException;

public class ClientDemo {
    public static void main(String[] args) throws IOException {
        new Thread(new NioClient("127.0.0.1", 2333)).start();
        new Thread(new NioClient("127.0.0.1", 2333)).start();
    }
}
