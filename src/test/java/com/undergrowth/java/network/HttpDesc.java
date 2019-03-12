package com.undergrowth.java.network;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description Http协议描述 http--超文本传输协议 请求和响应的格式--4部分 1.首部行--请求方法 资源路径 协议版本 请求方法--GET/POST/PUT/DELETE Http响应码-- 100--服务器准备接收主体(1xx--提示信息的响应)
 * 200--OK(2xx--指向成功) 304--资源未修改(3xx--资源重定向) 404--NOT FOUND(4xx--客户端错误) 500--Internal Server Error(5xx--服务器错误) 2.首部头--keyword:value
 * Accept:text/html,text/plain,image/gif,image/jpeg 8个顶级类型 text/*--人可读文字 image/*--表示图片 model/*--表示3D模型 audio/*--表示声音 video/*--表示视频
 * application/*--表示二进制数据 message/*--表示协议特定的信封 multipart/*--表示多个资源和文档的容器 3.空行 两个\r\n\r\n 4.消息体 eg:GET http://blog.csdn.net/undergrowth/article/details/44003877
 * HTTP/1.1 Cache-Control:private, max-age=0, must-revalidate Connection:keep-alive Content-Encoding:gzip Content-Type:text/html; charset=utf-8
 * Date:Sun, 15 Jan 2017 06:29:18 GMT ETag:W/"c9d7bfdccdd595174ab51db549b2f55d" Keep-Alive:timeout=20
 * @date 2017-01-15-14:25
 */
public class HttpDesc {

}
