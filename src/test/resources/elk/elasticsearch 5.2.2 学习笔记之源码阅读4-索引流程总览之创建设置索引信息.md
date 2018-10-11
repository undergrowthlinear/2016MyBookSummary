# elasticsearch 5.2.2 学习笔记之源码阅读4-索引流程总览之创建设置索引信息
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://blog.csdn.net/undergrowth/article/details/82851401
## 索引流程(本文将以6个方法讲述索引相关的流程)
- 创建设置索引信息
    - postman put http://localhost:9200/testindex
```
{
  "index": {
    "analysis": {
      "analyzer": {
        "by_synonym_smart": {
          "type": "custom",
          "tokenizer": "ik_smart",
          "filter": ["by_tfr","remote_synonym"],
          "char_filter": [
            "by_cfr"
          ]
        },
        "by_synonym_max_word": {
          "type": "custom",
          "tokenizer": "ik_max_word",
          "filter": ["by_tfr","remote_synonym"],
          "char_filter": [
            "by_cfr"
          ]
        }
      },
      "filter": {
        "by_tfr": {
          "type": "stop",
          "stopwords": [" "]
        },
        "remote_synonym": {
          "type" : "dynamic_synonym",
          "synonyms_path" : "同义词远程路径",
        "interval": 21600
        }
      },
      "char_filter": {
        "by_cfr": {
          "type": "mapping",
          "mappings": ["| => |","- => "]
        }
      }
    }
  }
}
```
- 查询索引信息
    - postman get http://localhost:9200/testindex/
- 设置索引类型的映射信息
    - postman put http://localhost:9200/testindex/testlog/_mapping
```
{
    	"properties": { 
        "mesg":    { "type": "text","analyzer":"by_synonym_smart"  }, 
        "user":     { "type": "text"  }, 
        "date":      { "type": "integer" }  
      }
}
```
- 添加索引类型数据
    - postman put http://localhost:9200/testindex/testlog
```
{
"date" : 12345,
"user" : "chenlin7",
"mesg" : "好好学习,天天向上,Elasticsearch,first message into Elasticsearch"
}
```
- 使用索引设置的分析器解析分析数据
    - postman get http://localhost:9200/testindex/_analyze?text=好好学习,天天向上&analyzer=by_synonym_smart
    - postman get http://localhost:9200/testindex/_analyze?text=好好学习,天天向上&analyzer=standard
- 删除索引
    - postman delete http://localhost:9200/testindex
### 核心接口与类
- 前置加载过程与 https://blog.csdn.net/undergrowth/article/details/82851401 类似,主要看各个Action的不同,核心看添加索引类型数据流程,涉及到调用lucene层信息,将数据写入lucene段等流程
- 本文涉及到的Action
    - 创建设置索引信息涉及Action
        - CreateIndexAction/RestCreateIndexAction/TransportCreateIndexAction
    - 查询索引信息
        - GetIndexAction/RestGetIndicesAction/TransportGetIndexAction
    - 设置索引类型的映射信息
        - PutMappingAction/RestPutMappingAction/TransportPutMappingAction
    - 添加索引类型数据
        - IndexAction/RestIndexAction/TransportIndexAction
    - 使用索引设置的分析器解析分析数据
        - AnalyzeAction/RestAnalyzeAction/TransportAnalyzeAction
    - 删除索引
        - DeleteIndexAction/RestDeleteIndexAction/TransportDeleteIndexAction
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - MetaDataCreateIndexService(负责提交索引请求)
            - MetaDataCreateIndexService#onlyCreateIndex
- IndexComponent
    - AbstractIndexComponent
        - IndexService(负责索引相关功能)
        - MapperService(负责映射相关功能)
- ClusterState(管理集群的状态以及各种元信息存放)
- MetaData(存放集群元信息)
- IndexMetaData(单个索引的元信息)
    - settings/mappings/aliases/numberOfShards/numberOfReplicas等信息
- RoutingTable(存放集群范围内所有索引的路由信息)
- TransportAction
    - HandledTransportAction(负责在传输层注册处理器处理消息)
        - TransportMasterNodeAction(封装需要在master节点才能操作的行为)
            - TransportCreateIndexAction(创建索引Action)
            - TransportPutMappingAction(放置映射文件Action)
            - TransportDeleteIndexAction(删除索引Action)
            - TransportMasterNodeReadAction(在master节点只读的操作)
                - TransportGetIndexAction(获取索引信息)
    - TransportReplicationAction(主节点以及副本的分片操作)
        - TransportWriteAction(分片操作中写行为的总称)
            - TransportIndexAction(执行索引操作,例如添加数据)
- Engine
    - InternalEngine(负责与Lucene层进行交互,使用IndexWriter负责索引数据的写,使用SearcherManager进行数据的读)
    - org.elasticsearch.index.engine.Engine.Operation(索引操作的封装)
        - org.elasticsearch.index.engine.Engine.Index
        - org.elasticsearch.index.engine.Engine.Delete
- Translog(记录所有未提交的索引操作记录)
### 以创建设置索引信息为例讲解加载过程
- postman put http://localhost:9200/testindex
    - 前置加载过程参看 https://blog.csdn.net/undergrowth/article/details/82851401
    - 分别来看CreateIndexAction/RestCreateIndexAction/TransportCreateIndexAction三者间的消息扭转
        - RestCreateIndexAction#prepareRequest解析request参数,构造CreateIndexRequest请求
        - TransportCreateIndexAction的父类TransportMasterNodeAction#doExecute创建AsyncSingleAction进行一些列的前置操作,调用TransportMasterNodeAction#masterOperation回调子类TransportCreateIndexAction#masterOperation
        - 调用MetaDataCreateIndexService#createIndex进行索引创建流程,涉及到进行参数校验validate,解析Alias,查找IndexTemplateMetaData,mappings等等,从而创建IndexMetaData,存放在MetaData,更新ClusterState,完成索引创建,实际上这里是没有进行Lucene层的操作,在讲解添加索引类型数据时候会涉及到lucene的操作,但是这里是创建了UpdateTask,进行状态的发布publishAndApplyChanges,使其他模块进行下一步操作,例如状态保存(GatewayMetaState#applyClusterState)
    


        
