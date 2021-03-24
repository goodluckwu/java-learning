package io.github.wuzhihao7.bio;

import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1", 8080);
        socket.getOutputStream().write("向服务器发送数据".getBytes());
        socket.close();
    }
}
