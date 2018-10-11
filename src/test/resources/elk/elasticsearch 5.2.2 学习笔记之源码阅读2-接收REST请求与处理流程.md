# elasticsearch 5.2.2 学习笔记之源码阅读2-HttpServerTransport/Action接收REST请求与处理流程
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
## 接收请求与处理流程
```
HttpServerTransport(使用netty接收用户请求,转发给HttpServer#dispatchRequest,进而利用RestController的RestHandler处理相应的用户请求)
```
### 模块设计
- elasticsearch的每一个功能都对应三个Action
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
            - Netty4HttpServerTransport(Netty4HttpRequestHandler请求处理器) 
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
### 查询集群状态详细代码流
- http://localhost:9200
    - 在node启动时候,调用Netty4HttpServerTransport#doStart监听用户rest请求,Netty4HttpRequestHandler#channelRead0处理器接收用户数据请求
    - 构建Netty4HttpRequest,Netty4HttpServerTransport#dispatchRequest分发请求给HttpServer#dispatchRequest
    - 在HttpServer里面也是通过RestController#dispatchRequest分发请求,RestController#getHandler实际上核心的是通过请求的方法和path找到相应的Action处理器,例如RestMainAction处理GET/HEAD方法,path为/的请求,这里使用的是前缀匹配的Trie树
    - 找到RestMainAction后,这里使用模板的设计模式,先调用父类的BaseRestHandler#handleRequest,回调各个Action的prepareRequest,使MainAction与RestMainAction进行了绑定,同时调用子类提供的action.accept方法,这里是AbstractClient#execute
    - 本地节点回调NodeClient#doExecute,NodeClient#executeLocally,通过NodeClient#transportAction的Action名称找到MainAction与TransportMainAction的映射(MainAction与TransportMainAction的绑定在ActionModule创建的时候绑定),执行TransportAction#execute的方法回调子类的action.doExecute,即TransportMainAction#doExecute查询clusterService的状态,以MainResponse的方式向通道回写数据
### 查询分词内容详细代码流
- http://localhost:9200/_analyze?text=你好
    - 接收请求的过程与上面一致,看下不同的RestAnalyzeAction/TransportAnalyzeAction/AnalyzeAction
    - 当获取到处理器RestAnalyzeAction#prepareRequest的时候,解析完参数后调用IndicesAdmin#analyze,类似的调用TransportAction#execute进而回调TransportAnalyzeAction的父类TransportSingleShardAction#doExecute创建AsyncSingleAction进行执行操作
    - 此时与上面不一样在于,这里会transportService.sendRequest发送transportShardAction请求,因为是local节点,获取TransportService#getConnection时获取localNodeConnection连接,调用TransportService#sendRequestInternal,回调Connection#sendRequest后续为sendLocalRequest找到对应Action的RequestHandlerRegistry为ShardTransportHandler,进行本地请求的接收与处理
    - 接收本地请求ShardTransportHandler#messageReceived后,回调TransportAnalyzeAction#shardOperation的分片操作,进而回调TransportAnalyzeAction#analyze找到对应的分析器/分词器/,进行分词TransportAnalyzeAction#simpleAnalyze,对词根据分析器进行分词,这里熟悉lucene的小伙伴就很熟悉了
    - 回写AnalyzeResponse响应,这种本地调用的方式,采用基于事件处理的方式进行处理,也很巧妙,很多搜索和索引请求都是基本此流程
    


        
