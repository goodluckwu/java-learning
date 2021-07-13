package io.github.wuzhihao7.integer;

import java.util.BitSet;

public class IntegerDemo {
    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE + 1);
        System.out.println(Integer.MAX_VALUE + Integer.MAX_VALUE);
        System.out.println(1 << 2);
        System.out.println(-1 >>> Integer.numberOfLeadingZeros(9 - 1));
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(1));
        System.out.println(Integer.toBinaryString(0));
        System.out.println(0b11111111111111111111111111111111);
        System.out.println(-2 >> 1);
        System.out.println(Integer.toBinaryString(-2));
        System.out.println(Integer.toBinaryString(-2 >> 1));
        System.out.println(Integer.toBinaryString((-2 >> 1) - 1));
        System.out.println(-2 >>> 1);
        System.out.println(Integer.toBinaryString(-2));
        System.out.println(Integer.toBinaryString(-2 >>> 1));
        System.out.println(Integer.toBinaryString((-2 >>> 1) - 1));
        System.out.println(0b1111111111111111111111111111111);
        System.out.println((byte)0b11111111);
        System.out.println((byte)0b10000000);
        System.out.println((byte)0b01111111);

        int n = 1 << 30;
        int rs = Integer.numberOfLeadingZeros(n) | (1 << (16 - 1));
        System.out.println(Integer.toBinaryString(Integer.numberOfLeadingZeros(n)));
        System.out.println(Integer.toBinaryString(1 << 15));
        System.out.println(Integer.toBinaryString(rs));
        System.out.println(Integer.toBinaryString(rs << 16));
        System.out.println(rs << 16);
        System.out.println(Integer.toBinaryString(-1));

    }
}
