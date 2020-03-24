package com.technologysia.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel channel = aFile.getChannel();

        //create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(48);

        //read into buffer.
        int bytesRead = channel.read(buf);
        while (bytesRead != -1){
            System.out.println("Read: "+bytesRead);

            //make buffer ready for read
            buf.flip();

            // read 1 byte at a time
            while (buf.hasRemaining()){
                System.out.print((char)buf.get());
            }
            System.out.println();

            //make buffer ready for writing
            buf.clear();
            bytesRead = channel.read(buf);
        }
        aFile.close();
    }
}
