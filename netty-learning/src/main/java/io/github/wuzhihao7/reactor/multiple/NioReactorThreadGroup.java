package io.github.wuzhihao7.reactor.multiple;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class NioReactorThreadGroup {
    private static final AtomicInteger requestCounter = new AtomicInteger();

    private final int nioThreadCount;
    private static final int DEFAULT_NIO_THREAD_COUNT;
    private final NioReactorThread[] nioThreads;

    static {
        DEFAULT_NIO_THREAD_COUNT = Runtime.getRuntime().availableProcessors() > 1
                      ? 2 * (Runtime.getRuntime().availableProcessors() - 1 ) : 2;
    }

    public NioReactorThreadGroup(){
        this(DEFAULT_NIO_THREAD_COUNT);
    }

    public NioReactorThreadGroup(int threadCount){
        if(threadCount < 1){
            threadCount = DEFAULT_NIO_THREAD_COUNT;
        }

        this.nioThreadCount = threadCount;
        this.nioThreads = new NioReactorThread[nioThreadCount];

        for(int i = 0; i<threadCount; i++){
            this.nioThreads[i] = new NioReactorThread();
            this.nioThreads[i].start();
        }

        System.out.println("Nio 线程数量：" + threadCount);
    }

    public void dispath(SocketChannel socketChannel){
        if(socketChannel != null){
            next().register(socketChannel);
        }
    }

    public NioReactorThread next(){
        return this.nioThreads[requestCounter.getAndIncrement() % nioThreadCount];
    }
}
