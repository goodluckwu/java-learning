package com.technologysia;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuzhihao
 */
public class EchoOutBoundHandler2 extends ChannelOutboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(EchoOutBoundHandler2.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf result = (ByteBuf)msg;
        byte[] bytes = new byte[result.readableBytes()];
        result.readBytes(bytes);

        String str = new String(bytes);
        log.info("EchoOutBoundHandler2 received-[{}]", str);

        result.writeBytes(str.getBytes());
        ctx.write(result);
    }
}
