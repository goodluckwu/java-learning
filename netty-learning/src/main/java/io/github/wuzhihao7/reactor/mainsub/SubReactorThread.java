package io.github.wuzhihao7.reactor.mainsub;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public class SubReactorThread extends Thread{
    private Selector selector;
    private ExecutorService businessExecutorPool;
    private List<NioTask> taskList = new ArrayList<>(512);
    private ReentrantLock taskMainLock = new ReentrantLock();

    public SubReactorThread(ExecutorService businessExecutorPool){
        try {
            this.businessExecutorPool = businessExecutorPool;
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(NioTask task){
        if(task != null){
            try {
                taskMainLock.lock();
                taskList.add(task);
            }finally {
                taskMainLock.unlock();
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                Set<SelectionKey> ops;
                try {
                    selector.select(1000);
                    ops = selector.selectedKeys();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                for(Iterator<SelectionKey> it = ops.iterator(); it.hasNext();){
                    SelectionKey key = it.next();
                    it.remove();

                    try {
                        if(key.isWritable()){
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            ByteBuffer buf = (ByteBuffer) key.attachment();
                            buf.flip();
                            clientChannel.write(buf);
                            System.out.println("服务端向客户端发送数据。。。");
                            clientChannel.register(selector,SelectionKey.OP_READ);
                        }else if(key.isReadable()){
                            System.out.println("服务端接收客户端连接请求。。。");
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            ByteBuffer buf = ByteBuffer.allocate(1024);
                            clientChannel.read(buf);
                            //转发请求到具体的业务线程；当然，这里其实可以向dubbo那样，支持转发策略，如果执行时间短，
                            //，比如没有数据库操作等，可以在io线程中执行。本实例，转发到业务线程池
                            dispatch(clientChannel, buf);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.channel().close();
                        key.cancel();
                    }
                }

                if(!taskList.isEmpty()){
                    try {
                        taskMainLock.lock();
                        for(Iterator<NioTask> it = taskList.iterator(); it.hasNext();){
                            NioTask task = it.next();
                            try {
                                SocketChannel sc = task.getSc();
                                if(task.getData() != null){
                                    sc.register(selector, task.getOp(), task.getData());
                                }else {
                                    sc.register(selector, task.getOp());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            it.remove();
                        }
                    }finally {
                        taskMainLock.unlock();
                    }
                }
            }catch (Throwable e){
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SocketChannel clientChannel, ByteBuffer buf) {
        this.businessExecutorPool.execute(new Handler(clientChannel, buf, this));
    }
}
