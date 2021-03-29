package io.github.wuzhihao7.netty.bytebuf.directbuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class DirectBufDemo {
    public static void main(String[] args) {
        ByteBuf directBuf = Unpooled.directBuffer();
        directBuf.writeBytes("abcd".getBytes(StandardCharsets.UTF_8));
        if(!directBuf.hasArray()){
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
        }
    }
}
