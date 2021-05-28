package io.github.wuzhihao7.juc.concurrenthashmap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapApiDemo {
    public static void main(String[] args) {
        Map<String, Integer> hashMap = new HashMap<>();
        Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        //key，value不能为空
        hashMap.put(null,1);
        hashMap.put("1",null);
//        concurrentHashMap.put(null,1);
//        concurrentHashMap.put("1",null);

        //如果key存在，计算返回null，则删除；如果不存在，且返回null，则不添加；如果不存在，返回非null，添加之
        concurrentHashMap.put("test",0);
        concurrentHashMap.put("test1",1);
        concurrentHashMap.compute("test",(k, v) -> null);
        concurrentHashMap.compute("test3",(k, v) -> null);
        concurrentHashMap.compute("test4",(k, v) -> 4);


        System.out.println(hashMap);
        System.out.println(concurrentHashMap);



    }
}
