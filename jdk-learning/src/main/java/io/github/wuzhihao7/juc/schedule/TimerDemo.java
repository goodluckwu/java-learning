package io.github.wuzhihao7.juc.schedule;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

public class TimerDemo {
    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();

        LocalDateTime startTime = LocalDateTime.now();
        long delay = 2000L;
        long period = 2000L;
        Date time = Date.from(startTime.plusSeconds(10).atZone(ZoneId.systemDefault()).toInstant());
        System.out.printf("%s - 定时任务开始, 延迟时间: %s, 时间间隔: %s, date: %s%n", startTime, delay, period, time);

        // 通过 Timer 定时定频率调用 MyTimerTask 的业务逻辑
//        timer.schedule(new MyTimerTask("1-schdule(task, time)"), time);
//        timer.schedule(new MyTimerTask("2-schedule(task, time, period)"), time, period);
//        timer.schedule(new MyTimerTask("3-schedule(task, delay)"), delay);
        timer.schedule(new MyTimerTask("4-schedule(task, delay, period)"), delay, period);
//        timer.scheduleAtFixedRate(new MyTimerTask("5-scheduleAtFixedRate(task, time, period)"), time, period);
        timer.scheduleAtFixedRate(new MyTimerTask("6-scheduleAtFixedRate(task, delay, period)"), delay, period);
    }
}
