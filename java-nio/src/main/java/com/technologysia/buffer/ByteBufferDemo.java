package com.technologysia.buffer;

import java.nio.ByteBuffer;

public class ByteBufferDemo {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'H').put((byte) 'E').put((byte) 'L').put((byte) 'L').put((byte) 'O');
        System.out.println(buffer.toString());
        buffer.flip();
        System.out.println(buffer.toString());
        for(int i=0; buffer.hasRemaining(); i++){
            System.out.print((char)buffer.get());
        }
        System.out.println();
        System.out.println(buffer.toString());
        buffer.flip();
        System.out.println(buffer.toString());
        int count = buffer.remaining();
        for(int i=0; i < count; i++){
            System.out.print((char)buffer.get());
        }
    }
}
