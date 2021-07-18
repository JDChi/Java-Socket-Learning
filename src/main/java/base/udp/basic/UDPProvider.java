package base.udp.basic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPProvider {
    public static void main(String[] args) throws IOException {
        System.out.println("UDPProvider started");
        // 接收者，指定一个端口用于数据接收
        DatagramSocket datagramSocket = new DatagramSocket(20000);

        // 构建接收实体
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
        // 接收
        datagramSocket.receive(receivePack);

        // 从接收到的信息里获取发送者的信息
        String senderIp = receivePack.getAddress().getHostAddress();
        int senderPort = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);

        System.out.println("UDPProvider receive from ip : " + senderIp + " port : " + senderPort + " data : " + data);

        // 构建回送数据
        String responseData = "Receive data with len : " + dataLen;
        byte[] responseDataBytes = responseData.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes, responseDataBytes.length, receivePack.getAddress(), receivePack.getPort());
        datagramSocket.send(responsePacket);

        System.out.println("UDPProvider Finished");
        datagramSocket.close();
    }
}
