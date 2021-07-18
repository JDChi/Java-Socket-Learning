package base.udp.basic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSearcher {
    public static void main(String[] args) throws IOException {
        System.out.println("UDPSearcher started");
        // 发送者，由系统分配端口
        DatagramSocket datagramSocket = new DatagramSocket();

        // 构建请求数据
        String sendData = "Hello World";
        byte[] sendDataBytes = sendData.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length,
                InetAddress.getLocalHost(), 20000);
        datagramSocket.send(sendPacket);


        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
        // 接收
        datagramSocket.receive(receivePack);

        // 从接收到的信息里获取发送者的信息
        String senderIp = receivePack.getAddress().getHostAddress();
        int senderPort = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);

        System.out.println("UDPSearcher receive from ip : " + senderIp + " port : " + senderPort + " data : " + data);


        System.out.println("UDPSearcher Finished");
        datagramSocket.close();
    }
}
