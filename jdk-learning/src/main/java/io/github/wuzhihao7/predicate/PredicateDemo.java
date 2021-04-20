package io.github.wuzhihao7.predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Adam", "Alexander", "John", "Tom");
        names.stream().filter(name -> name.startsWith("A")).forEach(System.out::println);
        System.out.println("-----------------------------");
        names.stream().filter(name -> name.startsWith("A")).filter(name -> name.length() < 5).forEach(System.out::println);
        System.out.println("-----------------------------");
        names.stream().filter(name -> name.startsWith("A") && name.length() < 5).forEach(System.out::println);
        System.out.println("-----------------------------");
        Predicate<String> p1 = name -> name.startsWith("A");
        Predicate<String> p2 = name -> name.length() < 5;
        names.stream().filter(p1.and(p2)).forEach(System.out::println);
        System.out.println("-----------------------------");
        names.stream().filter(p1.or(p2)).forEach(System.out::println);
        System.out.println("-----------------------------");
        names.stream().filter(p1.negate()).forEach(System.out::println);
        System.out.println("-----------------------------");
        names.stream().filter(Predicate.not(p1)).forEach(System.out::println);
        System.out.println("-----------------------------");
        names.stream().filter(Predicate.isEqual("Alexander")).forEach(System.out::println);
        System.out.println("-----------------------------");
        List<Predicate<String>> allPredicates = new ArrayList<>();
        allPredicates.add(str -> str.startsWith("A"));
        allPredicates.add(str -> str.contains("d"));
        allPredicates.add(str -> str.length() > 4);

        names.stream().filter(allPredicates.stream().reduce(x -> true, Predicate::and)).forEach(System.out::println);
        System.out.println("-----------------------------");
        names.stream().filter(allPredicates.stream().reduce(x -> false, Predicate::and)).forEach(System.out::println);

    }
}
