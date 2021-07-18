package base.tcp;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(3000);
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);
        System.out.println("已发送服务器连接");
        System.out.println("客户端信息 : " + socket.getLocalAddress() + " P : " + socket.getLocalPort());
        System.out.println("服务端信息 : " + socket.getInetAddress() + " P : " + socket.getPort());
        try {
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }

        socket.close();
        socket.shutdownInput();
        System.out.println("客户端已退出");

    }

    private static void todo(Socket client) throws IOException {
        // 构建键盘输入流
        InputStream systemInputStream = System.in;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(systemInputStream));

        // 得到 Socket 输入流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);
        // 获取 Socket 输入流，即服务端输入进来的
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // 轮询
        boolean flag = true;
        do {
            // 键盘读取一行
            String str = inputReader.readLine();
            // 发送到服务器
            socketPrintStream.println(str);

            // 从服务器读取一行
            String echo = socketBufferedReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);

        // 资源释放
        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
