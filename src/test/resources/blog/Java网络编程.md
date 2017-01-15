##2016书单总结--Java网络编程
1. 基础概念
    - 网络(network)--几乎可以实时的发送和接收数据的计算机以及其他设备的集合
    - 网络分层--应用层、传输层、网际层、网络层
    - 端口--(1--65535)/起到分流的作用/ftp(21)/ssh(22)/telnet(23)/http(80)
    - 网络地址分块--利用子网掩码进行处理
    - 网络地址转换(NAT)--内网ip和外网ip之间的转换
    - 防火墙--安全过滤业务包
    - 代理服务器--转发请求、回复响应
2. 流--字节流/字符流
    - 关于java流描述(http://blog.csdn.net/undergrowth/article/details/43820639)
3. 线程--单独、独立执行单元--详见
    - (http://blog.csdn.net/undergrowth/article/details/54237527)
    - (http://blog.csdn.net/undergrowth/article/details/54354602)
    - (http://blog.csdn.net/undergrowth/article/details/44003877)
4. UrlUri--
    - URI----资源标识符 模式:模式的特定部分
    - 模式----ftp/file/telnet/http/data/rmi/mailto/magnet/doc 特定部分----授权机构/资源路径?查询参数
     * eg:模式://授权机构/资源路径?查询参数
     * eg:http://china.huanqiu.com/article/2016-07/9145823.html?from=bdwz 模式----http
     * 授权机构----china.huanqiu.com 资源路径----/article/2016-07/9145823.html
     * 查询参数----from=bdwz
    - URL----资源定位符,既能标示资源又能定位资源 协议://用户信息@主机:端口/路径?查询参数#片段
     * eg:http://china.huanqiu.com/article/2016-07/9145823.html?from=bdwz
     * 相对URL(站内跳转)和绝对URL(站外跳转)
5. InternetAddress--
    * NAT------内外网地址转换
    * DNS------IP与域名的转换
    * IPV4----4分8字节十机制
    * IPV6----8分16字节十六进制
6. Http--
    * http--超文本传输协议
     * 请求和响应的格式--4部分
     * ---->首部行--请求方法 资源路径 协议版本
     *      请求方法--GET/POST/PUT/DELETE
     *      Http响应码--
    		100--服务器准备接收主体(1xx--提示信息的响应)
    		200--OK(2xx--指向成功)
    		304--资源未修改(3xx--资源重定向)
    		404--NOT FOUND(4xx--客户端错误)
    		500--Internal Server Error(5xx--服务器错误)
     * ---->首部头--keyword:value
           Accept:text/html,text/plain,image/gif,image/jpeg
           8个顶级类型
           text/*--人可读文字
           image/*--表示图片
           model/*--表示3D模型
           audio/*--表示声音
           video/*--表示视频
           application/*--表示二进制数据
           message/*--表示协议特定的信封
           multipart/*--表示多个资源和文档的容器*
     * ---->空行
     *      两个\r\n\r\n
     * ---->消息体
     * eg:GET http://blog.csdn.net/undergrowth/article/details/44003877 HTTP/1.1
        Cache-Control:private, max-age=0, must-revalidate
    	Connection:keep-alive
    	Content-Encoding:gzip
    	Content-Type:text/html; charset=utf-8
    	Date:Sun, 15 Jan 2017 06:29:18 GMT
    	ETag:W/"c9d7bfdccdd595174ab51db549b2f55d"
    	Keep-Alive:timeout=20

7. Socket--关于Socket描述
    - (http://blog.csdn.net/undergrowth/article/details/46707413)
    - (http://blog.csdn.net/undergrowth/article/details/46419473)
    - (http://blog.csdn.net/undergrowth/article/details/46363827)
8. 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git
关于URLURI的示例在com.undergrowth.java.network.UriUrl
关于InternetAddress的示例在com.undergrowth.java.network.InternetAddress