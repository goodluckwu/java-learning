package io.github.wuzhihao7.reactor.mainsub;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sub Reactor：目前没有使用jdk的并发池，这里用的SubReactorThreadGroup,其实现是数组，当然这里也可以使用jdk线程池，SubReactor的每一个线程都是IO线程，用来处理读，写事件。所有的IO线程公用一个业务线程池（基于juc）实现，用来处理业务逻辑，也就是运行Handel的地方。
 */
public class SubReactorThreadGroup {
    private static final AtomicInteger requestCounter = new AtomicInteger();

    private final int nioThreadCount;
    private static final int DEFAULT_NIO_THREAD_COUNT;
    private final SubReactorThread[] nioThreads;

    static {
        DEFAULT_NIO_THREAD_COUNT = Runtime.getRuntime().availableProcessors() > 1
                      ? 2 * (Runtime.getRuntime().availableProcessors() - 1 ) : 2;
    }

    public SubReactorThreadGroup(){
        this(DEFAULT_NIO_THREAD_COUNT);
    }

    public SubReactorThreadGroup(int threadCount) {
        if(threadCount < 1){
            threadCount = DEFAULT_NIO_THREAD_COUNT;
        }

        ExecutorService businessExecutePool = Executors.newFixedThreadPool(threadCount);

        this.nioThreadCount = threadCount;
        this.nioThreads = new SubReactorThread[threadCount];
        for(int i=0; i<threadCount; i++){
            this.nioThreads[i] = new SubReactorThread(businessExecutePool);
            this.nioThreads[i].start();
        }

        System.out.println("Nio 线程数量：" + threadCount);
    }

    public void dispatch(SocketChannel socketChannel){
        if(!Objects.isNull(socketChannel)){
            next().register(new NioTask(socketChannel, SelectionKey.OP_READ));
        }
    }

    private SubReactorThread next() {
        return this.nioThreads[requestCounter.getAndIncrement() % nioThreadCount];
    }
}
