# spring-cloud-sleuth集成zipkin之spring-cloud-1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
## 概述
- 参考
    - http://m.blog.csdn.net/forezp/article/details/76795269
    - https://github.com/forezp/SpringCloudLearning/tree/master/chapter-sleuth
    - https://github.com/spring-cloud/spring-cloud-sleuth
    - http://blog.csdn.net/undergrowth/article/details/78255897
## ZipkinAutoConfiguration---->zipkin支持
- 解析以spring.zipkin/spring.sleuth.sampler开头的参数
- 注入HttpZipkinSpanReporter---->ZipkinSpanReporter---->用于将Span信息发送到Zipkin服务器
    - 用RestTemplate以POST将span信息转换为字节码信息发出
      - 异步从ByteBoundedQueue队列发送Span过程如下:
        - zipkin.reporter.AsyncReporter.BoundedAsyncReporter#flush(zipkin.reporter.BufferNextMessage)
          - org.springframework.cloud.sleuth.zipkin.RestTemplateSender#sendSpans
- 注入ZipkinSpanListener---->SpanReporter的实现---->SpanReporter是sleuth与zikin的桥梁
    - 同步将Span放入ByteBoundedQueue队列:
      - org.springframework.cloud.sleuth.zipkin.ZipkinSpanListener#report
        - org.springframework.cloud.sleuth.zipkin.HttpZipkinSpanReporter#report
          - zipkin.reporter.AsyncReporter.BoundedAsyncReporter#report
## TraceAutoConfiguration---->sleuth核心支持
- 解析以spring.sleuth/spring.sleuth.keys开头的参数
- 注入DefaultTracer---->Tracer的实现,Tracer依赖SpanReporter(SpanReporter的实现ZipkinSpanListener)
## TraceWebAutoConfiguration---->sleuth web环境支持
- 注入TraceWebAspect---->进行相应的切面拦截
- 注入TraceFilter过滤器---->过滤servletRequest/servletResponse
    - 请求前,createSpan
    - 请求
    - 响应前,detachOrCloseSpans
      - tracer().close(span)
        - this.spanReporter.report(span)---->这里就是回调了ZipkinSpanListener.report将Span放入ByteBoundedQueue队列
## 测试
- 示例代码可参看位于-- https://github.com/forezp/SpringCloudLearning/tree/master/chapter-sleuth