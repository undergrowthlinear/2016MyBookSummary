# elasticsearch 5.2.2 学习笔记之源码阅读8-网关模块(GatewayModule)总览之state相关操作
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://blog.csdn.net/undergrowth/article/details/82851401
    - https://blog.csdn.net/undergrowth/article/details/82885628
    - https://blog.csdn.net/thomas0yang/article/details/52535604
## 网关模块(GatewayModule)总览之state相关操作
- 集群state查询
    - postman get http://localhost:9200/_cluster/state
- 单节点data目录下结构
    - 状态分为3个级别,全局级别/索引级别/索引分片级别
```
└─nodes
    └─0
        ├─indices
        │  └─bF6MEzfZT7OuLSdGA6flvg
        │      ├─0
        │      │  ├─index
        │      │  ├─translog
        │      │  └─_state
        │      ├─1
        │      │  ├─index
        │      │  ├─translog
        │      │  └─_state
        │      ├─2
        │      │  ├─index
        │      │  ├─translog
        │      │  └─_state
        │      ├─3
        │      │  ├─index
        │      │  ├─translog
        │      │  └─_state
        │      ├─4
        │      │  ├─index
        │      │  ├─translog
        │      │  └─_state
        │      └─_state
        └─_state
```
### 核心接口与类
```
本文主要关注state的读写以及恢复(只关注全局级别状态/索引级别状态,分片索引状态在AllocationService)
在索引添加时,集群状态变更后,状态如何保存到本次磁盘
在集群启动时,如何从本次磁盘加载集群状态
```
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - AbstractLifecycleComponent
            - GatewayService(网管服务类支持,进行集群状态的恢复)
        - GatewayMetaState(网关元数据)
        - GatewayMetaState(实际的处理读、写集群状态/索引状态的类)
- GatewayModule(网关模块,组合网关模块相关类)
- ClusterStateApplier(集群状态的处理者)
    - GatewayMetaState
- org.elasticsearch.env.NodeEnvironment.NodePath(存放数据/索引的相对目录)
    - ${data.paths}/nodes/{node.id}----节点目录
    - ${data.paths}/nodes/{node.id}/indices----索引目录
- org.apache.lucene.store.Directory#obtainLock(节点数据的锁,使用lucene的互斥锁(eg:node.lock))
- MetaDataStateFormat(状态信息与本地磁盘和lucene读写交互层,GatewayMetaState使用它进行操作)
    - MetaDataStateFormat#loadLatestState(加载指定路径的最新版本状态信息)
    - MetaDataStateFormat#write(写状态信息到指定目录)
### 以 索引添加时,集群状态变更后,状态如何保存到本次磁盘 为例讲解加载过程
- postman put http://localhost:9200/testindex
    - 前置加载过程参看 https://blog.csdn.net/undergrowth/article/details/82851401
    - 创建索引过程参看 https://blog.csdn.net/undergrowth/article/details/82885628
    - 在创建索引的时候,clusterService.submitStateUpdateTask提交,最终ClusterService#submitStateUpdateTasks创建UpdateTask任务,执行
    - 运行ClusterService.UpdateTask#run/ClusterService#runTasks,在ClusterService#calculateTaskOutputs里通过executeTasks执行任务(这里实际上是执行了MetaDataCreateIndexService#onlyCreateIndex定义的匿名AckedClusterStateUpdateTask类,返回新的ClusterState),通过patchVersionsAndNoMasterBlocks对比任务返回的集群状态信息与原有集群状态信息对比,在master节点集群状态版本号+1,返回新的集群状态
    - 通过taskOutputs.clusterStateUnchanged()比较previousClusterState/newClusterState状态,当添加索引,状态变更，通过publishAndApplyChanges发布集群状态变更,先通知其他节点通过clusterStatePublisher.accept,调用ZenDiscovery#publish进行集群状态的发布,其他节点也就获取到了(详细的其他节点获取状态信息进行变更的过程在Discovery内容再进行讲解),在其他节点进行余下类似的操作,本地调用callClusterStateAppliers通知相应的ClusterStateApplier处理者,例如GatewayMetaState
    - 接收到集群状态变更回调到GatewayMetaState#applyClusterState,如果是全局信息变更,则进行metaStateService.writeGlobalState状态写入,同时也获取需要变更状态的索引,调用metaStateService.writeIndex进行写入索引状态,都是调用MetaStateService#writeIndex进而调用MetaDataStateFormat#write相关类进行磁盘的写入
### 以 集群启动时,如何从本次磁盘加载集群状态 为例讲解加载过程
- 以创建GatewayMetaState为入口进行集群状态的加载
    - 如果是主节点或者是数据节点(DiscoveryNode.isMasterNode(settings) || DiscoveryNode.isDataNode(settings)),则进行metaStateService.loadFullState()状态信息的加载
    - MetaStateService#loadFullState分为加载loadGlobalState和加载索引的IndexMetaData.FORMAT.loadLatestState状态
    - 通过MetaData.FORMAT.loadLatestState加载NodeEnvironment#nodeDataPaths目录下的_state文件
    - 通过IndexMetaData.FORMAT.loadLatestState加载NodeEnvironment#resolveIndexFolder目录下的_state文件
    


        
