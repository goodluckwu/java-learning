package com.technologysia.mainsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Acceptor.class);

    private final ServerSocketChannel serverSocketChannel;

    /**
     * CPU核心数
     */
    private final int coreNum = Runtime.getRuntime().availableProcessors();
    /**
     * 创建Selector给SubReactor调用
     */
    private final Selector[] selectors = new Selector[coreNum];
    /**
     * 轮询使用SubReactor的下标索引
     */
    private int next = 0;
    /**
     * SubReactor
     */
    private SubReactor[] reactors = new SubReactor[coreNum];
    /**
     * SubReactor的处理线程
     */
    private Thread[] threads = new Thread[coreNum];

    public Acceptor(ServerSocketChannel serverSocketChannel) throws IOException {
        this.serverSocketChannel = serverSocketChannel;
        for(int i = 0; i < coreNum; i++){
            selectors[i] = Selector.open();
            //初始化SubReactor
            reactors[i] = new SubReactor(selectors[i], i);
            //初始化运行SubReactor的线程
            threads[i] = new Thread(reactors[i]);
            //启动
            threads[i].start();
        }
    }

    @Override
    public void run() {
        SocketChannel socketChannel;
        try {
            //接收连接
            socketChannel = serverSocketChannel.accept();
            if(socketChannel != null){
                log.info("accept {}", socketChannel.getRemoteAddress());
                socketChannel.configureBlocking(false);

                //注意一个selector在select时是无法注册新事件的，因此这里要先暂停下select方法触发的程序段，
                //下面的wakeup和这里的setRestart都是做这个事情的，具体参考SubReactor的run方法
                reactors[next].setRegister(true);
                //使一个阻塞住的select操作立即返回
                selectors[next].wakeup();
                //注册一个读事件
                SelectionKey selectionKey = socketChannel.register(selectors[next], SelectionKey.OP_READ);
                //使一个阻塞住的select操作立即返回
                selectors[next].wakeup();

                //本次事件注册完成后，需要再次触发select的执行，
                //因此，这里Restart要再设置为false
                reactors[next].setRegister(false);
                //绑定Handler
                selectionKey.attach(new AsyncHandler(socketChannel, selectors[next], next));
                if(++next == selectors.length){
                    //越界后重新分配
                    next = 0;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
