# UDP 是什么
- 一种用户数据报协议，又称用户数据报文协议
- 是一个简单的面向数据报的传输层协议，正式规范为 RFC 768
- 用户数据协议、非连接协议

# 为什么不可靠
- 它一旦把应用程序发给网络层的数据发送出去，就不保留数据备份
- UDP 在 IP 数据报的头部仅仅加入了复用和数据校验（字段）
- 发送端生产数据，接收端从网络中抓取数据
- 结构简单、无校验、速度快、容易丢包、可广播

UDP 使用 DatagramSocket 类。因为是数据报协议，在传递的时候使用 DatagramPacket 类做传输。

# UDP 包格式
UDP 包是 64 位，0 - 15 是源端口，16 - 31 是目的端口，32 - 47 是数据长度，48 - 64 是头部加数据

## UDP 包最大长度
- 16 位 -> 2 字节，存储长度信息
- 2 ^ 16 - 1 = 64k - 1 = 65536 - 1 = 65535
- 自身协议占用：32 + 32 位 = 64 位 = 8 字节
- 65535 - 8 = 65507 byte
如果传输的数据超过了 65507 byte ，那就需要分包。
  
# 核心 API
## DatagramSocket
- 用于接收与发送 UDP 的类
- 负责发送某一个 UDP 包，或者接收 UDP 包
- 不同于 TCP，UDP 并没有合并到 Socket API 中
- DatagramSocket() 创建简单实例，不指定端口与 IP
- DatagramSocket(int port) 创建监听固定端口的实例
- DatagramSocket(int port , InetAddress localAddr) 创建固定端口指定 IP 的实例
- receive(DatagramPacket d) 接收
- send(DatagramPacket d) 发送
- setSoTimeout(int timeout) 设置超时，毫秒
- close 关闭、释放资源

## DatagramPacket
- 用于处理报文
- 将 byte 数组、目标地址、目标端口等数据包装成报文或者将报文拆卸成 byte 数组
- 是 UDP 的发送实体、接收实体
- DatagramPacket(byte buf[], int offset, int length,
  InetAddress address, int port)
    - 前 3 个参数指定 buf 的使用区间
    - 后 2 个参数指定目标机器地址与端口，只在于发送时有效，接收时无效
- setData(byte[] buf, int offset, int length)
- setLength(int length)