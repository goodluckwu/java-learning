package io.github.wuzhihao7.codec;

import java.nio.charset.StandardCharsets;

public class StringCodecDemo {
    public static void main(String[] args) {
        unicode();
    }

    public static void unicode(){
        String str = "我爱中国 China";
        for(int i=0; i < str.length(); i++){
            char c = str.charAt(i);
            System.out.printf("%c %s%n", c, Integer.toHexString(c));
        }
    }
}
