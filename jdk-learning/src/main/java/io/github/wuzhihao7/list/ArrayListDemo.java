package io.github.wuzhihao7.list;

import java.util.List;
import java.util.stream.Collectors;

public class ArrayListDemo {
    public static void main(String[] args) {
        System.out.println(List.of("1", "2", "3", "3", "4").stream().distinct().collect(Collectors.toList()));
    }
}
