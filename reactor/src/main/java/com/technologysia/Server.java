package com.technologysia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private final Selector selector = new Selector();
    private final Dispatcher dispatcher = new Dispatcher(selector);
    private final Acceptor acceptor;

    public Server(int port) {
        this.acceptor = new Acceptor(port, selector);
    }

    public void start(){
        new Thread(() -> {
            log.info("服务启动");
            dispatcher.registEventHandler(EventType.ACCEPT, new AcceptEventHandler(selector));
            new Thread(acceptor, "Acceptor-" + acceptor.getPort()).start();
            dispatcher.handleEvents();
        }).start();
    }

    public void addConnection(InputSource source){
        acceptor.addNewConnection(source);
    }

    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
        IntStream.range(0 ,10).forEach(i -> {
            server.addConnection(new InputSource("data"+i, i));
        });
    }
}
