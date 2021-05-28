package io.github.wuzhihao7.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 如果key存在，不覆盖，返回当前value；如果key不存在，添加，返回null
 */
public class putIfAbsentDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);

        Integer four = map.putIfAbsent("4", 5);
        System.out.println(four);

        System.out.println("----------");

        Integer five = map.putIfAbsent("5", 5);
        System.out.println(five);

        System.out.println(map);
    }
}
