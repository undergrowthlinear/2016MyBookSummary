# okhttp3.8.1 学习笔记
## 概述
- 参考
  - http://www.jianshu.com/p/1873287eed87
  - http://www.cnblogs.com/jianyungsun/p/6648390.html
  - http://blog.csdn.net/u012124438/article/details/54236967
- okhttp类似于httpcomponent组件,基于Request/Response模型,利用Call封装不同的Request,利用Interceptor.Chain对Request/Response进行不同的拦截
- CallServerInterceptor进行最后的request请求到远程主机,处理响应
- 最终委托给HttpCodec来将Request的头与消息体写入Sink中,从Source中获取消息体转换为Response
## Request/Response
- Request---->请求
  - An HTTP request. Instances of this class are immutable if their {@link #body} is null or itself immutable.
  - HttpUrl/method/headers/RequestBody---->包含请求的请求行、请求头、请求体
    - FormBody---->RequestBody(以application/x-www-form-urlencoded传递数据)
    - HttpEntityBody---->RequestBody(以application/octet-stream传递数据,适配httpcomponent-core的httpentity)
    - okhttp3.RequestBody#create(okhttp3.MediaType, java.lang.String)---->自定义头传递,eg:application/json
    - 请求体最终往okio.BufferedSink中写入请求体数据
- Response---->响应
  - An HTTP response. Instances of this class are not immutable: the response body is a one-shot value that may be consumed only once and then closed. All other properties are immutable
  - code/Protocol/ResponseBody---->返回的状态码、协议、响应体
    - RealResponseBody---->ResponseBody
    - 从okio.BufferedSource中读取数据返回响应体
## Call---->请求动作
- A call is a request that has been prepared for execution. A call can be canceled. As this object represents a single request/response pair (stream), it cannot be executed twice
- RealCall---->Call
  - okhttp3.RealCall#getResponseWithInterceptorChain
    - 委托给Interceptor.Chain执行请求
## Interceptor.Chain---->请求/响应链支持
- CallServerInterceptor---->Interceptor
- RealInterceptorChain---->Interceptor.Chain
  - CallServerInterceptor---->位于拦截器链的最后一个,负责网络请求到对端网络,处理响应,均委托给HttpCodec进行处理
  - RealInterceptorChain---->提供运行整个拦截链机制,
  - 递归调用okhttp3.internal.http.RealInterceptorChain#proceed(okhttp3.Request, okhttp3.internal.connection.StreamAllocation, okhttp3.internal.http.HttpCodec, okhttp3.internal.connection.RealConnection)
## HttpCodec---->请求/响应编解码支持
- Encodes HTTP requests and decodes HTTP responses
- Http2Codec---->HttpCodec
  - writeRequestHeaders/createRequestBody---->写请求头/请求体到Sink
  - readResponseHeaders/openResponseBody---->从Source中解析响应头/响应体
## Sink/Source
- BufferedSink---->Sink(输出缓存槽)
- BufferedSource---->Source(输入缓存槽)
## OkHttpClient---->okhttp入口
- Factory for {@linkplain Call calls}, which can be used to send HTTP requests and read their responses.
## StreamAllocation---->支持连接/请求/调用的一致性
- okhttp3.internal.connection.StreamAllocation#newStream---->创建编解码器
- okhttp3.internal.connection.StreamAllocation#findHealthyConnection---->使用连接池创建连接
## 测试
- okhttp3.guide.GetExample
- okhttp3.guide.PostExample
- okhttp3.recipes.PostForm
- okhttp3.recipes.RequestBodyCompression
- okhttp3.sample.Crawler---->简单爬虫示例