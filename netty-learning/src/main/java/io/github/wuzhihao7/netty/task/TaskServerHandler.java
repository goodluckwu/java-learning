package io.github.wuzhihao7.netty.task;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TaskServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TaskServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String s = new String(bytes);
        if("quit".equals(s)){
            ctx.channel().parent().close().sync();
        }

        log.info("channelRead start");
        ctx.channel().eventLoop().execute(() -> {
            log.info("执行耗时操作");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("执行耗时结束");
        });
        log.info("执行耗时操作2 开始0");
        ctx.channel().eventLoop().schedule(() -> {
            log.info("执行耗时操作2");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("执行耗时结束2");
        }, 5, TimeUnit.SECONDS);
        log.info("执行耗时操作3 开始");
        ctx.channel().eventLoop().schedule(() -> {
            log.info("执行耗时操作3");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("执行耗时结束3");
        }, 5, TimeUnit.SECONDS);
        log.info("channelRead end");
    }
}
