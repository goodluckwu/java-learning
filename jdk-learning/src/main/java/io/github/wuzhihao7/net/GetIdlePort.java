package io.github.wuzhihao7.net;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class GetIdlePort {
    public static void main(String[] args) throws Exception {
        InetSocketAddress randomSocketAddressFirst = new InetSocketAddress("10.213.32.156", 0);

        try (ServerSocket ssOne = new ServerSocket()) {
            System.out.println("randomSocketAddress port before any binding : " + randomSocketAddressFirst.getPort());
            ssOne.bind(randomSocketAddressFirst);
            System.out.println("local port after first binding :" + ssOne.getLocalPort());
        }

        try (ServerSocket ssTwo = new ServerSocket()) {
            ssTwo.bind(randomSocketAddressFirst);
            System.out.println("local port after second binding :" + ssTwo.getLocalPort());
            System.out.println("randomSocketAddress port after all bindings : " + randomSocketAddressFirst.getPort());
        }
    }
}
