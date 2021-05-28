package io.github.wuzhihao7.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 不管key存不存在，对value进行操作，返回新value
 */
public class ComputeDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);

        BiFunction<String, Integer, Integer> remappingFunction = (key, value) -> {
            System.out.println(key + ":" + value);
            if (value == null) {
                return 0;
            }

            return value + 1;
        };

        Integer four = map.compute("4", remappingFunction);
        System.out.println(four);

        System.out.println("----------");

        Integer five = map.compute("5", remappingFunction);
        System.out.println(five);

        System.out.println(map);
    }
}
