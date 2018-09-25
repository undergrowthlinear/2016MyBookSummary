# elasticsearch 5.2.2 学习笔记之源码阅读2-HttpServerTransport/Action接收请求与处理流程
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
## 接收请求与处理流程
```
HttpServerTransport(使用netty接收用户请求,转发给HttpServer#dispatchRequest,进而利用RestController的RestHandler处理相应的用户请求)
```
### 模块设计
- elasticsearch的每一个功能都对应三个个Action
- eg1:查询集群状态
    - 三个Action分别为MainAction和TransportMainAction,RestMainAction
    - 查询集群状态对应于上面三个Action进行转换    http://localhost:9200
- eg2:查询分词内容
    - 三个Action分别为AnalyzeAction/TransportAnalyzeAction/RestAnalyzeAction
    - http://localhost:9200/_analyze?text=你好
### 核心接口与类
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
- Releasable(Closeable)
    - LifecycleComponent(start/stop/addLifecycleListener)
        - HttpServerTransport
            - Netty4HttpServerTransport 
        - AbstractLifecycleComponent
            - HttpServer
    - AbstractComponent
        - RestController
- Action(用于关联TransportAction与BaseRestHandler的纽带,定义行为名称)
    - MainAction(cluster:monitor/main)
    - AnalyzeAction(indices:admin/analyze)
- TransportAction(用于处理实际的行为)
    - TransportMainAction
    - TransportAnalyzeAction
- BaseRestHandler(用于接收用户请求,子类定义处理请求路径)
    - RestMainAction
    - RestAnalyzeAction
    


        
