package io.github.wuzhihao7.singleton;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class RegisterSingleton {
    private static final Map<String, RegisterSingleton> MAP = new ConcurrentHashMap<>();

    private RegisterSingleton(String name) {
    }

    public static RegisterSingleton getInstance(String name){
        return MAP.computeIfAbsent(name, RegisterSingleton::new);
    }

    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
        final CountDownLatch start = new CountDownLatch(1);
        int count = 3000;
        final CountDownLatch end = new CountDownLatch(count);
        ExecutorService exce = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            Runnable run = () -> {
                try {
                    start.await();
                    // 测试执行具体的方法
                    list.add(RegisterSingleton.getInstance(RegisterSingleton.class.getName()).toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    end.countDown();
                }
            };
            exce.submit(run);
        }
        start.countDown();
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exce.shutdown();
        while (true){
            try {
                if (exce.awaitTermination(1, TimeUnit.MINUTES)) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        System.out.println(list.stream().distinct().count());
    }
}
