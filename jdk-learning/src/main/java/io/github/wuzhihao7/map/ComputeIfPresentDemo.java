package io.github.wuzhihao7.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 对在map中已有key的value进行操作，返回新value;如果key不存在，则不操作，返回null；
 */
public class ComputeIfPresentDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);

        BiFunction<String, Integer, Integer> remappingFunction = (key, value) -> {
            System.out.println(key + ":" + value);
            if(value == null){
                return 0;
            }
            return value + 1;
        };

        Integer four = map.computeIfPresent("4", remappingFunction);
        System.out.println(four);

        System.out.println("----------");

        Integer five = map.computeIfPresent("5", remappingFunction);
        System.out.println(five);

        System.out.println(map);
    }
}
