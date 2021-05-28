package io.github.wuzhihao7.juc.volatiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class VolatileDemo {
    volatile int count = 0;

    public int get(){
        return count;
    }

    public void set(int count){
        this.count = count;
    }

    public void getAndIncrement() {
        count++;
    }


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10000);
        VolatileDemo volatileDemo = new VolatileDemo();
        IntStream.range(0,10000).forEach(i -> {
            executorService.execute(() -> {
                volatileDemo.getAndIncrement();
            });
        });
        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)){
            System.out.println("等待结束");
        }
        System.out.println(volatileDemo.get());
    }
}
