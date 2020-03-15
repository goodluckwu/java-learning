package com.techonologysia;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TimeServerHandler.class);

    /**
     * As explained, the channelActive() method will be invoked when a connection is established and ready to generate traffic（产生流量）.
     * Let's write a 32-bit integer that represents（代表） the current time in this method.
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //To send a new message, we need to allocate a new buffer which will contain the message.
        // We are going to write a 32-bit integer, and therefore we need a ByteBuf whose capacity is at least 4 bytes.
        // Get the current ByteBufAllocator via ChannelHandlerContext.alloc() and allocate a new buffer.
        final ByteBuf time = ctx.alloc().buffer(4);
        //2208988800L为网络时间和系统时间的时间差
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        //As usual, we write the constructed message.
        //
        //But wait, where's the flip? Didn't we used to call java.nio.ByteBuffer.flip() before sending a message in NIO?
        // ByteBuf does not have such a method because it has two pointers;
        // one for read operations and the other for write operations.
        // The writer index increases when you write something to a ByteBuf while the reader index does not change.
        // The reader index and the writer index represents where the message starts and ends respectively（分别）.
        //In contrast（相比之下）, NIO buffer does not provide a clean way to figure out（确定） where the message content starts and ends without calling the flip method.
        // You will be in trouble when you forget to flip the buffer because nothing or incorrect data will be sent.
        // Such an error does not happen in Netty because we have different pointer for different operation types.
        // You will find it makes your life much easier as you get used to it -- a life without flipping out（发疯）!
        //Another point to note is that the ChannelHandlerContext.write() (and writeAndFlush()) method returns a ChannelFuture.
        // A ChannelFuture represents an I/O operation which has not yet（还没有） occurred.
        // It means, any requested operation might not have been performed yet because all operations are asynchronous in Netty.
        // For example, the following code might close the connection even before a message is sent:
        /*
         * Channel ch = ...;
         * ch.writeAndFlush(message);
         * ch.close();
         */
        //Therefore, you need to call the close() method after the ChannelFuture is complete,
        // which was returned by the write() method, and it notifies its listeners when the write operation has been done.
        // Please note that, close() also might not close the connection immediately（立即）, and it returns a ChannelFuture.
        final ChannelFuture f = ctx.writeAndFlush(time);

        //How do we get notified when a write request is finished then?
        // This is as simple as adding a ChannelFutureListener to the returned ChannelFuture.
        // Here, we created a new anonymous（匿名） ChannelFutureListener which closes the Channel when the operation is done.
        //Alternatively, you could simplify the code using a pre-defined listener:f.addListener(ChannelFutureListener.CLOSE);
//        f.addListener((ChannelFutureListener) future -> {
//            assert f == future;
//            ctx.close();
//        });
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
