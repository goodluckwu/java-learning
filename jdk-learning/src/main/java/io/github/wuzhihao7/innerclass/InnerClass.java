package io.github.wuzhihao7.innerclass;

import java.io.IOException;

public class InnerClass {
    private void run(){
        System.out.println("InnerClass.run");
    }

    public void run2(){
        System.out.println("InnerClass.run2");
        new Thread(InnerClass.this::run).start();
    }

    public static void main(String[] args) throws IOException {
        final InnerClass innerClass = new InnerClass();
        innerClass.run2();
        System.in.read();
    }
}
