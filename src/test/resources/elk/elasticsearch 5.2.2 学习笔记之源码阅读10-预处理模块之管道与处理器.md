# elasticsearch 5.2.2 学习笔记之源码阅读10-预处理插件(IngestPlugin)之管道与处理器
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/ingest.html
    - https://www.felayman.com/articles/2017/12/09/1512830107116.html
    - http://www.cnblogs.com/liang1101/p/7284205.html
## 预处理插件(IngestPlugin)之管道与处理器
- 预处理节点(Ingest Node)
    - 进行文档预处理
    - org.elasticsearch.cluster.node.DiscoveryNode.Role中定义了节点的三种角色,分别为MASTER/DATA/INGEST
- 管道(Pipeline)
    - 用于定义一系列的处理器
- 处理器(Processor)
    - 用于对文档进行变换/预处理,充当elk组件中logstash功能
- 第一步,创建管道 postman put http://localhost:9200/_ingest/pipeline/my_set_pipeline
```
{
  "description" : "describe pipeline",
  "processors" : [
    {
      "set" : {
        "field": "foo",
        "value": "bar"
      }
    }
  ]
}
```
- 第二步,模拟管道中的处理器处理文档过程 postman post http://localhost:9200/_ingest/pipeline/_simulate?verbose
```
{
  "pipeline" :
  {
    "description": "_description",
    "processors": [
      {
        "set" : {
          "field" : "field2",
          "value" : "_value"
        }
      },
      {
        "set" : {
          "field" : "field3",
          "value" : "_value"
        }
      }
    ]
  },
  "docs": [
    {
      "_index": "index",
      "_type": "type",
      "_id": "id",
      "_source": {
        "foo": "bar"
      }
    }
  ]
}
```
- 在添加文档数据时使用管道 postmant post http://localhost:9200/testindex/testlog?pipeline=my_set_pipeline
```
{
"date" : 12345,
"user" : "chenlin7",
"mesg" : "好好学习,天天向上,Elasticsearch,first message into Elasticsearch"
}
```  
### 核心接口与类
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - AbstractLifecycleComponent
        - PipelineStore(将创建的管道添加到集群状态中,更新集群状态)
    - LifecycleComponent
- IngestService(用于预处理相关的支持类)
- IngestPlugin(插件扩展,用于加载自定义处理器)
    - IngestCommonPlugin(es默认的预处理插件)
- Pipeline(拥有一系列的处理器和description/processors/version/on_failure/id属性)
    - Pipeline.Factory(根据传入的相关属性创建管道,会验证创建的处理器是否存在等)
- Processor(处理器基类)
    - AbstractProcessor
        - org.elasticsearch.ingest.CompoundProcessor(组合模式的处理器)
        - org.elasticsearch.ingest.common.SetProcessor(添加或者修改字段值)
            - overrideEnabled/field/value
- IngestDocument(代表单行文档,拥有其元信息和值/_ingest)
- IngestInfo
- ProcessorInfo
### 以 创建管道/模拟管道使用 为例讲解加载过程
- node启动查看  https://blog.csdn.net/undergrowth/article/details/82840411
    - 通过PluginsService(https://blog.csdn.net/undergrowth/article/details/82857089)加载modules目录下的ingest-common插件,
    即加载IngestCommonPlugin用于构建IngestService,在创建IngestService时候,
    通过IngestCommonPlugin#getProcessors加载内置的DateProcessor/SetProcessor/JsonProcessor等各种处理器
    - 创建管道涉及到的Action分别为 PutPipelineAction/RestPutPipelineAction/PutPipelineTransportAction
        - RestPutPipelineAction构建PutPipelineRequest请求,添加超时设置,参看前面文章,请求扭转到PutPipelineTransportAction#masterOperation,
        先获取集群各节点信息后,通过PipelineStore#put/innerPut先validatePipeline判断,使用Pipeline.Factory#create创建Pipeline,
        然后构建新的ClusterState,然后发布集群状态,后续跟前一篇发现模块的集群状态发布流程一致
    - 模拟管道使用涉及到的Action分别为 SimulatePipelineAction/RestSimulatePipelineAction/SimulatePipelineTransportAction
        - RestSimulatePipelineAction构建SimulatePipelineRequest,
        消息扭转SimulatePipelineTransportAction#doExecute后创建SimulatePipelineRequest进行执行
        - 后续遍历请求中的IngestDocument,调用SimulateExecutionService#executeDocument使用CompoundProcessor遍历其中的processors进行处理,
        例如这里的SetProcessor#execute
### 以 在添加文档数据时使用管道 为例讲解加载过程
- 添加索引类型数据流程参看 https://blog.csdn.net/undergrowth/article/details/82899315
    - 当使用 postmant post http://localhost:9200/testindex/testlog?pipeline=my_set_pipeline 携带pipeline看下有什么不同
        - 在TransportIndexAction#doExecute开头就有判断是否有pipeline参数,同时判断如果本地节点是预处理节点的话,则进行预处理操作processIngestIndexRequest
        - 实际上调用ingestService服务的PipelineExecutionService#executeIndexRequest处理IndexRequest请求,先获取管道然后PipelineExecutionService#innerExecute
            - 先构建IngestDocument
            - 接着Pipeline#execute后org.elasticsearch.ingest.CompoundProcessor#execute跟模拟操作一样
            - 然后回写indexRequest.source相关内容