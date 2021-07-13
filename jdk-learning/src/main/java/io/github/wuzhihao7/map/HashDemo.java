package io.github.wuzhihao7.map;

import java.util.HashMap;
import java.util.Objects;

public class HashDemo {
    @Override
    public int hashCode() {
//        return 1;
        return super.hashCode();
    }

    public static void main(String[] args) {
        HashDemo hashDemo = new HashDemo();
        System.out.println(hashDemo.hashCode());
        System.out.println(System.identityHashCode(hashDemo));
        System.out.println(Objects.hashCode(hashDemo));

        for(int i = 0; i < 10; i++){
            System.out.println(i);
        }

        int cap = 31;
        int MAXIMUM_CAPACITY = 128;
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        System.out.println((n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1);

        System.out.println(0 ^ 0);
    }
}
