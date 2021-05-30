package io.github.wuzhihao7.juc.countdownlatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class SimulateConcurrencyDemo {
    private static final Logger log = LoggerFactory.getLogger(SimulateConcurrencyDemo.class);

    public static void main(String[] args) {
        Runnable task = new Runnable() {
            private int counter;

            @Override
            public void run() {
                for(int i=0; i < 10; i++){
                    counter++;
                    log.info("{}", counter);
                }
            }
        };
        startTaskAllInOnce(100,task);
    }

    public static void startTaskAllInOnce(final int threadNum, final Runnable task){
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(threadNum);
        for(int i=0; i < threadNum; i++){
            new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    }finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        long startTime = System.nanoTime();
        log.info("startTime-[{}], All thread is ready, concurrent going...", startTime);
        startGate.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        log.info("endTime-[{}], All thread is completed.", endTime);
        log.info("cost: {}", Duration.ofNanos(endTime - startTime).toMillis());
    }
}
