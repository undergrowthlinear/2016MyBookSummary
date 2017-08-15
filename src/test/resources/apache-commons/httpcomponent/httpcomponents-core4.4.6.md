# apache- httpcomponents-core4.4.6 学习笔记
## 概述
````
http协议的基础与核心工具类,使用HttpMessage(包含RequestLine/StatusLine、Header、HttpEntity)在HttpConnection上进行传输，
在传递消息之前或者之后可采用HttpRequestInterceptor/HttpResponseInterceptor对消息进行处理
````
## HttpMessage---->消息基础,HttpRequest与HttpResponse均有此派生
````
       generic-message = start-line
                        (message-header CRLF)
                         CRLF
                        [ message-body ]
       start-line      = Request-Line | Status-Line
````
### HttpRequest---->请求
- BasicHttpEntityEnclosingRequest---->HttpEntityEnclosingRequest---->HttpRequest---->HttpMessage
- BasicHttpEntityEnclosingRequest---->BasicHttpRequest---->AbstractHttpMessage---->HttpMessage
  - HttpEntityEnclosingRequest提供请求中携带HttpEntity
  - AbstractHttpMessage提供Header相关方法支持
  - BasicHttpRequest提供RequestLine的支持
    - BasicHttpEntityEnclosingRequest拥有RequestLine/HttpEntity/Header
### HttpEntity---->消息体(请求消息体或者响应消息体)
- 包含三种类型
 * 支持数据流方式,不可重复(简单理解为数据需要从别处获取)
 * 自我包含方式,可重复(简单理解为数据在内存)
 * 封装模式,支持上面两种
- InputStream getContent()
- writeTo(OutputStream outstream)
- InputStreamEntity---->AbstractHttpEntity---->HttpEntity
  - A streamed, non-repeatable entity that obtains its content from an {@link InputStream}.
- StringEntity---->AbstractHttpEntity---->HttpEntity
  - A self contained, repeatable entity that obtains its content from a {@link String}
### HttpResponse---->响应
- BasicHttpResponse---->AbstractHttpMessage---->HttpResponse
  - BasicHttpResponse拥有StatusLine/HttpEntity/Header
## HttpConnection---->传输通道
- DefaultBHttpClientConnection---->HttpClientConnection---->HttpConnection
- DefaultBHttpClientConnection---->BHttpConnectionBase---->HttpInetConnection---->HttpConnection
  - HttpConnection拥有服务端和客户端拥有的通用方法----打开/关闭等
  - HttpClientConnection用于客户端连接,用于发送消息头、消息体、接收消息头、消息体
  - HttpInetConnection提供了在ip层支持
  - BHttpConnectionBase提供服务端与客户端连接的基本功能----输入输出流，委托给SessionInputBufferImpl/SessionOutputBufferImpl
    - DefaultBHttpClientConnection在以上的基本上提供了消息的输出与解析工作,委托给HttpMessageWriter与HttpMessageParser
## 拦截器---->对请求预处理再发送,对响应预处理载返给调用者
- ImmutableHttpProcessor---->HttpProcessor---->HttpRequestInterceptor
- ImmutableHttpProcessor---->HttpProcessor---->HttpResponseInterceptor
  - ImmutableHttpProcessor基础的链式处理
- RequestDate---->HttpRequestInterceptor
  - 添加日期消息头
## ConnPool---->连接池,管理共享的连接信息,相对于主机而言,主要有lease和release,有借有还
- BasicConnPool---->AbstractConnPool---->ConnPool
- BasicConnPool---->AbstractConnPool---->ConnPoolControl
- BasicPoolEntry---->PoolEntry
- RouteSpecificPool---->PoolEntry
  - ConnPool提供借与还的功能
  - AbstractConnPool提供实际创建PoolEntry功能,委托给RouteSpecificPool,
    - 最终通过org.apache.http.impl.pool.BasicConnPool.createEntry创建BasicPoolEntry,映射HttpHost与HttpClientConnection关系
  - 实际创建连接(final C conn = this.connFactory.create(route))
    - org.apache.http.pool.AbstractConnPool.getPoolEntryBlocking
    - 通过传入的连接工厂创建连接
## HttpRequestExecutor---->基于阻塞的IO执行请求与响应
- preProcess---->利用HttpProcessor对请求进行预处理
- execute---->doSendRequest
  - 发送请求(委托HttpClientConnection先发送sendRequestHeader,然后发送sendRequestEntity)
    - 委托给DefaultHttpRequestWriter---->AbstractMessageWriter---->HttpMessageWriter进行预处理,最终调用DefaultBHttpClientConnection相关方法进行数据的最终传输
- execute---->doReceiveResponse
  - 接收请求(委托HttpClientConnection先接收receiveResponseHeader,然后接收receiveResponseEntity)
    - 委托给DefaultHttpResponseParser---->AbstractMessageParser---->HttpMessageParser进行预处理,最终调用DefaultBHttpClientConnection相关方法进行数据的最终传输
- postProcess---->利用HttpProcessor对响应进行预处理
## 测试
- example例子
  - examples.org.apache.http.examples.ElementalHttpGet
  - examples.org.apache.http.examples.ElementalHttpPost
  - examples.org.apache.http.examples.ElementalPoolingHttpGet
    - 实际创建连接(final C conn = this.connFactory.create(route))
          - org.apache.http.pool.AbstractConnPool.getPoolEntryBlocking
          - 通过传入的连接工厂创建连接