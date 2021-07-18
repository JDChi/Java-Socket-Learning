# TCP 是什么
- 是传输控制协议，是一种面向连接的，可靠的、基于字节流的传输层通信协议，由 IETF 的 RFC 793 定义
- 与 UDP 一样完成第四层传输层所指定的功能与职责

TCP 的机制
- 三次握手、四次挥手
- 具有校验机制、可靠、数据传输稳定

# 核心 API
- socket() : 创建一个 Socket，这是在客户端上的，服务端上的是 ServerSocket()
- bind() : 绑定一个 Socket 到一个本地地址和端口上
- connect() : 连接到远程套接字
- accept() : 接受一个新的连接
- write() : 把数据写入到 Socket 的输出流
- read() : 从 Socket 输入流读取数据

由于有客户端和服务端的区分，客户端使用的是 Socket 类，服务端使用的是 ServerSocket 类。

