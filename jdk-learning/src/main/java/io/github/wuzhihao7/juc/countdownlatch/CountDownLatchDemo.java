package io.github.wuzhihao7.juc.countdownlatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
 */
public class CountDownLatchDemo {
    private static final Logger log = LoggerFactory.getLogger(CountDownLatchDemo.class);
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            log.info("子线程{}正在执行", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("子线程{}执行完毕", Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            log.info("子线程{}正在执行", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("子线程{}执行完毕", Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();

        try {
            log.info("等待两个子线程执行完毕...");
            countDownLatch.await();
            log.info("两个子线程已经执行完毕");
            log.info("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
