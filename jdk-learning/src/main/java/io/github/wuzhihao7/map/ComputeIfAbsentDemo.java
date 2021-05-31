package io.github.wuzhihao7.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 当key存在返回当前value值，不存在则执行函数，返回新value
 */
public class ComputeIfAbsentDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);

        Function<String, Integer> remappingFunction = key -> {
            System.out.println("key=" + key);
            return Integer.MAX_VALUE;
        };

        Integer four = map.computeIfAbsent("4", remappingFunction);
        System.out.println(four);

        System.out.println("----------");

        Integer five = map.computeIfAbsent("5", remappingFunction);
        System.out.println(five);

        Integer six = map.computeIfAbsent("6", key -> null);
        System.out.println(six);

        System.out.println(map);
    }
}
