package com.technologysia.mainsub;

import java.io.IOException;

public class MainSubReactorDemo {
    public static void main(String[] args) throws IOException {
        new Thread(new MainReactor(2333)).start();
    }
}
