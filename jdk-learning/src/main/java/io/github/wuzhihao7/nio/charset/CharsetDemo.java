package io.github.wuzhihao7.nio.charset;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

public class CharsetDemo {
    public static void main(String[] args) throws CharacterCodingException {
        Charset utf8 = StandardCharsets.UTF_8;
        //获取编码器
        CharsetEncoder encoder = utf8.newEncoder();
        //获取解码器
        CharsetDecoder decoder = utf8.newDecoder();

        //构造需要解码编码的数据
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("字符集编码解码");
        charBuffer.flip();

        //编码
        ByteBuffer encode = encoder.encode(charBuffer);
        System.out.println("编码后：");
        for(int i = 0; i < encode.limit(); i++){
            System.out.println(encode.get());
        }

        //解码
        encode.flip();
        CharBuffer decode = decoder.decode(encode);
        System.out.println("解码后：");
        System.out.println(decode);

        System.out.println("指定其他格式解码：");
        Charset gbk = Charset.forName("gbk");
        encode.flip();
        CharBuffer decode1 = gbk.decode(encode);
        System.out.println(decode1);
    }
}
