package com.technologysia.mainsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class SubReactor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SubReactor.class);

    private final Selector selector;
    /**
     * 序号，也就是Acceptor初始化SubReactor时的下标
     */
    private final int num;

    /**
     * 注册开关表示
     */
    private boolean register = false;

    public SubReactor(Selector selector, int num) {
        this.selector = selector;
        this.num = num;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            log.info("No{} SubReactor Waitting For Register...", num);
            while (!Thread.interrupted() && !register){
                try {
                    if(selector.select() == 0){
                        continue;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    dispatch(selectionKey);
                }
                selectionKeys.clear();
            }
        }
    }

    private void dispatch(SelectionKey selectionKey) {
        Runnable r = (Runnable) selectionKey.attachment();
        if(r != null){
            r.run();
        }
    }

    public void setRegister(boolean register){
        this.register = register;
    }
}
