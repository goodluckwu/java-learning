package io.github.wuzhihao7.netty.bytebuf.heafbuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class HeafByteBufDemo {
    public static void main(String[] args) {
        ByteBuf heafByteBuf = Unpooled.copiedBuffer("abcd".getBytes(StandardCharsets.UTF_8));
        if (heafByteBuf.hasArray()) {
            byte[] array = heafByteBuf.array();
            int offset = heafByteBuf.arrayOffset() + heafByteBuf.readerIndex();
            int length = heafByteBuf.readableBytes();
            handleArray(array, offset, length);
        }

    }

    private static void handleArray(byte[] array, int offset, int length) {
        System.out.println(new String(array, offset, length));
    }
}
