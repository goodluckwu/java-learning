package io.github.wuzhihao7.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {
    private static final AtomicInteger poolId = new AtomicInteger();

    public static void main(String[] args) {
        System.out.println(poolId.incrementAndGet());
    }
}
