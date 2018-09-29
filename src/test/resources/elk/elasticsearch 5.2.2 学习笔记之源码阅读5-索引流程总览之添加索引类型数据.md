# elasticsearch 5.2.2 学习笔记之源码阅读5-索引流程总览之添加索引类型数据
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://blog.csdn.net/undergrowth/article/details/82851401
    - https://blog.csdn.net/undergrowth/article/details/82885628
## 索引流程总览之添加索引类型数据 接上一篇 elasticsearch 5.2.2 学习笔记之源码阅读4-索引流程总览之创建设置索引信息
- 添加索引类型数据
    - postman put http://localhost:9200/testindex/testlog
```
{
"date" : 12345,
"user" : "chenlin7",
"mesg" : "好好学习,天天向上,Elasticsearch,first message into Elasticsearch"
}
```
- 查询索引数据
    - postman put http://localhost:9200/testindex/testlog/_search
```
{
    "query": {
        "query_string": {
            "query": "好好学习"
        }
    }
}
```
### 核心接口与类
- 前置加载过程与 https://blog.csdn.net/undergrowth/article/details/82851401 类似,主要看各个Action的不同,核心看添加索引类型数据流程,涉及到调用lucene层信息,将数据写入lucene段等流程
- 本文涉及到的Action
    - 添加索引类型数据
        - IndexAction/RestIndexAction/TransportIndexAction
- TransportAction
    - TransportReplicationAction(主节点以及副本的分片操作)
        - TransportWriteAction(分片操作中写行为的总称)
            - TransportIndexAction(执行索引操作,例如添加数据)
- TransportRequestHandler(传输层的请求处理器)
    - PrimaryOperationTransportHandler(处理主分片操作/transportPrimaryAction)
    - ReplicaOperationTransportHandler(处理副本操作/transportReplicaAction)
- Engine
    - InternalEngine(负责与Lucene层进行交互,使用IndexWriter负责索引数据的写,使用SearcherManager进行数据的读)
    - org.elasticsearch.index.engine.Engine.Operation(索引操作的封装)
        - org.elasticsearch.index.engine.Engine.Index
        - org.elasticsearch.index.engine.Engine.Delete
- Translog(记录所有未提交的索引操作记录)
### 以添加索引类型数据为例讲解加载过程
- postman put http://localhost:9200/testindex/testlog
    - 前置加载过程参看 https://blog.csdn.net/undergrowth/article/details/82851401
    - 分别来看IndexAction/RestIndexAction/TransportIndexAction三者间的消息扭转
        - 入口从RestIndexAction#prepareRequest开始构建IndexRequest转换到TransportIndexAction#doExecute,索引已存在调用innerExecute进而回调TransportIndexAction的父类TransportReplicationAction#doExecute创建ReroutePhase
        - 先找到IndexMetaData进行必要参数检验,查看主分片是否属于节点是否属local,则执行performLocalAction,不然performRemoteAction,这里本地调试走performLocalAction/performAction发送transportPrimaryAction转到PrimaryOperationTransportHandler处理器进行处理
        - PrimaryOperationTransportHandler#messageReceived接收到消息后创建AsyncPrimaryAction进行处理,如果不是处理分片中,则创建ReplicationOperation进行处理主分片和副本操作
        - ReplicationOperation处理主分片PrimaryShardReference#perform,回调TransportWriteAction#shardOperationOnPrimary后再回调TransportIndexAction#onPrimaryShard从而执行调用executeIndexRequestOnPrimary执行索引在主分片
        - 通过prepareIndexOperationOnPrimary分片信息创建索引操作Engine.Index,执行索引操作indexShard.index,调用InternalEngine#index/InternalEngine#innerIndex/执行indexWriter.addDocuments操作,添加数据进入索引,同时写入操作进入Translog通过maybeAddToTranslog方法
        - ReplicationOperation处理完主分片信息后,通过ReplicationOperation#performOnReplicas执行副本分片的拷贝,例如(多节点才能看到后续情况)调试环境performOnReplica,调用ReplicasProxy#performOn发送transportReplicaAction请求,进而ReplicaOperationTransportHandler处理器接收到消息,创建AsyncReplicaAction进行副本的操作,TransportWriteAction#shardOperationOnReplica/TransportIndexAction#onReplicaShard/TransportIndexAction#executeIndexRequestOnReplica创建REPLICA的操作,余下跟主分片写入一致,写入相应的索引信息
       
    


        
