package io.github.wuzhihao7.juc.concurrenthashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentHashMapThreadSafeDemo {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        List<Integer> sumList = parallelSum100(map,100);

        System.out.println(sumList);
        System.out.println(sumList.stream().distinct().count());
        System.out.println(sumList.stream().filter(num -> num != 100).count());
    }

    private static List<Integer> parallelSum100(Map<String, Integer> map, int executionTimes) throws InterruptedException{
        List<Integer> sumList = new ArrayList<>(1000);
        for(int i = 0;i < executionTimes; i++){
            map.put("test", 0);
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            for(int j = 0; j < 10; j++){
                executorService.execute(() -> {
                    for(int k = 0; k < 10; k++){
                        map.computeIfPresent("test",(key, value) -> value + 1);
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
            sumList.add(map.get("test"));
        }
        return sumList;
    }
}
