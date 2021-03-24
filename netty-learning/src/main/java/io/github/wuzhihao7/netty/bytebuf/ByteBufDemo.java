package io.github.wuzhihao7.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufDemo {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello".getBytes());
        byte[] bytes = new byte[3];
        ByteBuf byteBuf1 = byteBuf.readBytes(bytes);
        System.out.println(byteBuf == byteBuf1);
        ByteBuf byteBuf2 = byteBuf.readBytes(1);
        System.out.println(byteBuf == byteBuf2);

        ByteBuf buffer = Unpooled.buffer(1);
        ByteBuf byteBuf3 = byteBuf.readBytes(buffer);
        System.out.println(buffer == byteBuf3);
        System.out.println(byteBuf == byteBuf3);
    }
}
