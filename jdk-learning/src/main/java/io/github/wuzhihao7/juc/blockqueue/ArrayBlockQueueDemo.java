package io.github.wuzhihao7.juc.blockqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ArrayBlockQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            IntStream.range(0,Integer.MAX_VALUE).forEach(i -> {
                try {
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }).start();
        while (true){
            System.out.println("111");
            Integer take = queue.take();
        }

    }
}
