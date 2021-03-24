package io.github.wuzhihao7.bio;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务器已经启动并监听8080端口");
        while (true){
            System.out.println();
            System.out.println("服务器正在等待连接。。。");
            Socket socket = serverSocket.accept();
            System.out.println("服务器已经收到连接请求...");
            System.out.println();
            System.out.println("服务器正在等待数据。。。");
            byte[] buffer = new byte[1024];
            socket.getInputStream().read(buffer);
            System.out.println("服务器已接收到数据");
            System.out.println();
            String content = new String(buffer);
            System.out.println("接收到的数据：" + content);
        }
    }
}
