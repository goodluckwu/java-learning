package io.github.wuzhihao7.juc.semaphore;

import io.github.wuzhihao7.sequence.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限。
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        //工人数
        final int N = 8;
        //机器数目
        Semaphore semaphore = new Semaphore(5);
        System.out.println(semaphore.availablePermits());
        IntStream.range(0, N).forEach(i -> {
            new Worker(i, semaphore).start();
            System.out.println(i);
        });
        System.out.println("done");
        Semaphore semaphore1 = new Semaphore(0);
        System.out.println(semaphore1.availablePermits());
        semaphore1.release();
        System.out.println(semaphore1.availablePermits());
    }

    static class Worker extends Thread {
        private static final Logger log = LoggerFactory.getLogger(Worker.class);

        private final int num;
        private final Semaphore semaphore;

        public Worker(int num, Semaphore semaphore){
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                log.info("工人{}占用一个机器在生产...", this.num);
                TimeUnit.SECONDS.sleep(2L);
                log.info("工人{}释放出机器", this.num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        }
    }
}
