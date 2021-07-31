package io.github.wuzhihao7.limit;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SlideWindow {
    private static volatile Map<String, LinkedList<Long>> MAP = new ConcurrentHashMap<>();

    private SlideWindow(){}

    public static synchronized boolean isGo(String listId, int count, long timeWindow){
        long currentTime = System.currentTimeMillis();
        LinkedList<Long> list = MAP.computeIfAbsent(listId, k -> new LinkedList<>());

        if(list.size() < count){
            list.addFirst(currentTime);
            return true;
        }

        Long farTime = list.getLast();
        if(currentTime - farTime <= timeWindow){
            return false;
        }else{
            list.removeLast();
            list.addFirst(currentTime);
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            // 任意10秒内，只允许2次通过
            System.out.println(LocalTime.now().toString() + ": " + SlideWindow.isGo("ListId", 2, 500L));
            // 睡眠0-10秒
            Thread.sleep(new Random().nextInt(100));
        }
    }
}
