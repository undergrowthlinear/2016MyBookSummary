# elasticsearch 5.2.2 学习笔记之源码阅读6-索引流程总览之删除索引
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://blog.csdn.net/undergrowth/article/details/82851401
    - https://blog.csdn.net/undergrowth/article/details/82885628
## 索引流程总览之删除索引 接上一篇 elasticsearch 5.2.2 学习笔记之源码阅读5-索引流程总览之添加索引类型数据
- 删除索引
    - postman delete http://localhost:9200/testindex
### 核心接口与类
- 前置加载过程与 https://blog.csdn.net/undergrowth/article/details/82851401 类似,主要看各个Action的不同
- 本文涉及到的Action
    - 删除索引
        - DeleteIndexAction/RestDeleteIndexAction/TransportDeleteIndexAction
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - MetaDataDeleteIndexService(负责提交删除索引请求)
        - AllocationService(管理集群中节点的分片行为)
        - DestructiveOperations(辅助操作类)
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
            - TransportDeleteIndexAction(删除索引Action)
### 以删除索引为例讲解加载过程
- postman delete http://localhost:9200/testindex
    - 前置加载过程参看 https://blog.csdn.net/undergrowth/article/details/82851401
    - 分别来看DeleteIndexAction/RestDeleteIndexAction/TransportDeleteIndexAction三者间的消息扭转
        - 通过RestDeleteIndexAction#prepareRequest创建DeleteIndexRequest请求,借助NodeClient#executeLocally扭转到TransportDeleteIndexAction#doExecute
        - 创建AsyncSingleAction执行删除删除索引请求,进而回调TransportMasterNodeAction#masterOperation和TransportDeleteIndexAction#masterOperation,依赖org.elasticsearch.cluster.metadata.MetaDataDeleteIndexService#deleteIndices完成相关索引信息的更新与操作
        - 从集群中删除索引信息MetaDataDeleteIndexService#deleteIndices,例如进行快照检查SnapshotsService.checkIndexDeletion,从路由表中删除routingTableBuilder.remove,集群数据块中删除clusterBlocksBuilder.removeIndexBlocks,元信息中删除metaDataBuilder.remove,同时数据重新分片allocationService.reroute
    


        
