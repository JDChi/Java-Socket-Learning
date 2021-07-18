package base.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.UUID;

public class UDPProvider {
    public static void main(String[] args) throws IOException {
        // 生成唯一标识
        String sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn);
        provider.start();

        // 键盘任意输入就退出
        System.in.read();
        provider.exit();

    }

    private static class Provider extends Thread {
        private final String sn;
        private boolean done = false;
        private DatagramSocket datagramSocket = null;

        public Provider(String sn) {
            this.sn = sn;
        }

        @Override
        public void run() {
            super.run();

            System.out.println("UDPProvider started");
            try {
                // 监听 20000 端口
                datagramSocket = new DatagramSocket(20000);
                while (!done) {

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

                    // 解析端口号
                    int responsePort = MessageCreator.parsePort(data);
                    if (responsePort != -1) {
                        // 构建回送数据
                        String responseData = MessageCreator.buildWithSn(sn);
                        byte[] responseDataBytes = responseData.getBytes();
                        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes, responseDataBytes.length,
                                receivePack.getAddress(), responsePort);
                        datagramSocket.send(responsePacket);
                    }

                }
            } catch (Exception e) {

            } finally {
                System.out.println("UDPProvider Finished");
                close();
            }


        }

        private void close() {
            if (datagramSocket != null) {
                datagramSocket.close();
                datagramSocket = null;
            }
        }

        void exit() {
            done = true;
            close();
        }
    }
}
