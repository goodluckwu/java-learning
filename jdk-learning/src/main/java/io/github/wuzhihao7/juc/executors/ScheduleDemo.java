package io.github.wuzhihao7.juc.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleDemo {
    private static final Logger log = LoggerFactory.getLogger(ScheduleDemo.class);

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        log.info("执行定时任务1");
        scheduledExecutorService.schedule(()->{
            log.info("执行定时任务1开始");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("执行定时任务1结束");
        },5, TimeUnit.SECONDS);

        log.info("执行定时任务2");
        scheduledExecutorService.schedule(()->{
            log.info("执行定时任务2开始");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("执行定时任务2结束");
        },5, TimeUnit.SECONDS);

        scheduledExecutorService.shutdown();
        while (!scheduledExecutorService.awaitTermination(Integer.MAX_VALUE,TimeUnit.SECONDS)){
            System.out.println("等待任务结束");
        }
    }
}
