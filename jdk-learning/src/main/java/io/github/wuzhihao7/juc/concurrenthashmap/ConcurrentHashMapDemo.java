package io.github.wuzhihao7.juc.concurrenthashmap;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, String> m = new ConcurrentHashMap<>();

        System.out.println(m.put(100, "Hello"));
        System.out.println(m.put(101, "Geeks"));
        System.out.println(m.put(102, "Geeks"));

        //key存在，不修改
        System.out.println(m.putIfAbsent(101, "Hello"));

        System.out.println(m.remove(101, "Geeks"));
        System.out.println(m.remove(102, "Geekss"));

        System.out.println(m.replace(101, "Hello", "For"));

        System.out.println(m.putIfAbsent(103, "Hello"));
        System.out.println(m.replace(103, "Hello", "For"));

        System.out.println(m);
    }
}
