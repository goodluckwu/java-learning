package io.github.wuzhihao7.codec;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class StringCodecDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "我爱中国 China";
        System.out.println("转unicode");
        unicode(str);
        System.out.println("转utf8");
        utf8(str);
        gb2312(str);
    }

    private static void gb2312(String str) throws UnsupportedEncodingException {
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            byte[] bytes = (c + "").getBytes("GB2312");
            String tmp = "";
            for(byte b : bytes){
                tmp += Integer.toHexString(b & 0x00ff) + " ";
            }
            System.out.printf("%s %s%n", c, tmp);
        }
    }

    private static void utf8(String str) {
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            byte[] bytes = (c + "").getBytes(StandardCharsets.UTF_8);
            String tmp = "";
            for(byte b : bytes){
                tmp += Integer.toHexString(b & 0x00ff) + " ";
            }
            System.out.printf("%s %s%n", c, tmp);
        }
    }

    public static void unicode(String str){
        for(int i=0; i < str.length(); i++){
            char c = str.charAt(i);
            System.out.printf("%c %s%n", c, Integer.toHexString(c));
        }
    }
}
