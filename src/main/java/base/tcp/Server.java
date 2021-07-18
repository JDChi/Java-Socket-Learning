package base.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("服务器准备就绪");
        System.out.println("服务端信息 : " + serverSocket.getInetAddress() + " P : " + serverSocket.getLocalPort());

        // 等待客户端连接
        for (; ; ) {
            // 得到客户端
            Socket clientSocket = serverSocket.accept();
            // 构建异步线程用于处理多个到来的客户端
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
        }


    }

    // 定义一个线程类
    private static class ClientHandler extends Thread {
        private Socket socket;
        private boolean flag = true;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接 : " + socket.getInetAddress() + " P : " + socket.getPort());
            try {
                // 得到打印流，用于数据输出；服务器回送数据使用
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                // 得到输入流，用于接收数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                do {
                    // 从客户端拿到数据
                    String str = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(str)) {
                        flag = false;
                        // 回送 bye
                        socketOutput.println("bye");
                    } else {
                        System.out.println(str);
                        socketOutput.println("接收到的数据长度为 : " + str.length());
                    }

                } while (flag);
                socketInput.close();
                socketOutput.close();

            } catch (Exception e) {
                System.out.println("连接异常断开");
            } finally {
                // 关闭连接
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端已关闭 : " + socket.getInetAddress() + " P : " + socket.getPort());
        }
    }
}
