package io.github.wuzhihao7.juc.cyclicbarrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Writer;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
 */
public class CyclicBarrierDemo {
    private static final Logger log = LoggerFactory.getLogger(CyclicBarrierDemo.class);

    public static void main(String[] args) {
        final int N = 4;
        //当四个线程都到达barrier状态后，会从四个线程中选择一个线程去执行Runnable。
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, ()->log.info("执行完成后处理"));
        for(int i = 0; i < N; i++){
            new Writer(cyclicBarrier).start();
        }
    }

    static class Writer extends Thread {
        private static final Logger log = LoggerFactory.getLogger(Writer.class);

        private final CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            log.info("线程{}正在写入数据...", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                log.info("线程{}写入数据完毕，等待其他线程写入完毕", Thread.currentThread().getName());
                cyclicBarrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }
            log.info("所有线程处理完毕");
        }
    }
}
