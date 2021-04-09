package io.github.wuzhihao7.juc.schedule;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MyTimerTask extends TimerTask {
    private String name;

    public MyTimerTask(String name){
        this.name = name;
    }

    @Override
    public void run() {
        System.out.printf("%s - %s - %s, 开始执行, %s%n", LocalDateTime.now(), Thread.currentThread().getName(), name, LocalDateTime.ofInstant(Instant.ofEpochMilli(this.scheduledExecutionTime()), ZoneId.systemDefault()));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s - %s - %s, 执行结束%s%n", LocalDateTime.now(), Thread.currentThread().getName(), name, LocalDateTime.ofInstant(Instant.ofEpochMilli(this.scheduledExecutionTime()), ZoneId.systemDefault()));
    }
}
