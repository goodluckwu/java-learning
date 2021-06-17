package io.github.wuzhihao7.dp;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci {
    public static void main(String[] args) {
        int n = 4;
        System.out.println(fibonacci1(n));
        Map<Integer, Integer> map = new HashMap<>();
        System.out.println(fibonacci2(n, map));
        System.out.println(fibonacci3(n, map));
        System.out.println(fibonacci4(n));
    }

    private static int fibonacci4(int n) {
        if(n == 0 || n == 1){
            return 1;
        }
        int previous = 1, current = 1;
        for(int i = 2; i <= n; i++){
            int sum = previous + current;
            previous = current;
            current = sum;
        }
        return current;
    }

    private static int fibonacci3(int n, Map<Integer, Integer> map) {
        map.put(0,1);
        map.put(1,1);
        for(int i = 2; i <= n; i++){
            map.put(i,map.get(i-1) + map.get(i-2));
        }
        return map.get(n);
    }

    private static int fibonacci2(int n, Map<Integer, Integer> cache) {
        if(n ==0 || n == 1){
            return 1;
        }
        if(cache.containsKey(n)){
            return cache.get(n);
        }
        cache.put(n, fibonacci2(n-1, cache) + fibonacci2(n-2, cache));
        return cache.get(n);
    }

    private static int fibonacci1(int n) {
        if(n ==0 || n == 1){
            return 1;
        }
        return fibonacci1(n-1) + fibonacci1(n-2);
    }
}
