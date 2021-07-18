package base.udp.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UDPSearcher {
    private static final int LISTEN_PORT = 30000;


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("UDPSearcher started");

        Listener listener = listen();
        sendBroadcast();

        System.in.read();
        List<Device> devices = listener.getDevicesAndClose();
        for (Device device : devices) {
            System.out.println("Device : " + device.toString());
        }

        System.out.println("UDPSearcher finished");

    }

    private static Listener listen() throws InterruptedException {
        System.out.println("UDPSearcher start listen");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, countDownLatch);
        listener.start();
        countDownLatch.await();
        return listener;
    }

    /**
     * 发送广播
     */
    private static void sendBroadcast() throws IOException {
        System.out.println("UDPSearcher sendBroadcast started");
        // 发送者，由系统分配端口
        DatagramSocket datagramSocket = new DatagramSocket();

        // 构建请求数据
        String sendData = MessageCreator.buildWithPort(LISTEN_PORT);
        byte[] sendDataBytes = sendData.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length,
                InetAddress.getByName("255.255.255.255"), 20000);
        datagramSocket.send(sendPacket);
        datagramSocket.close();
        System.out.println("UDPSearcher sendBroadcast finished");
    }

    private static class Device {
        final int port;
        final String ip;
        final String sn;

        public Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    private static class Listener extends Thread {

        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private final List<Device> devices = new ArrayList();
        private DatagramSocket datagramSocket = null;
        private boolean done = false;

        public Listener(int listenPort, CountDownLatch countDownLatch) {
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override

        public void run() {
            super.run();
            // 通知已启动
            countDownLatch.countDown();

            try {

                datagramSocket = new DatagramSocket(listenPort);
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
                    String sn = MessageCreator.parseSn(data);
                    if (sn != null) {
                        Device device = new Device(senderPort, senderIp, sn);
                        devices.add(device);
                    }

                }

            } catch (Exception e) {

            } finally {
                close();
            }

            System.out.println("UDPSearcer listener finished");
        }

        private void close() {
            if (datagramSocket != null) {
                datagramSocket.close();
                datagramSocket = null;
            }
        }

        List<Device> getDevicesAndClose() {
            done = true;
            close();
            return devices;
        }
    }
}
