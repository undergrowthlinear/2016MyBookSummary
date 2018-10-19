# elasticsearch 5.2.2 学习笔记之源码阅读12-分片服务之分片_副本_路由
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/index-modules-allocation.html
    - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/shards-allocation.html
    - https://www.cnblogs.com/kangoroo/p/7622957.html
    - https://my.oschina.net/TOW/blog/1542018
    - https://blog.csdn.net/thomas0yang/article/details/78485067
## 分片服务之分片_副本_路由
- 分片(Shard)
    - 分片ID(ShardId,记录分片所属索引和索引中的分片id)
    - 分片路由(ShardRouting,根据集群最新状态,记录此分片的路由信息)
    - 分片索引信息(IndexShardState,记录分片是处于什么状态,是CREATED/POST_RECOVERY/RECOVERING/STARTED/RELOCATED/CLOSED)
    - 恢复状态(RecoveryState)
- 副本
- 路由(RoutingTable,记录集群中所有索引的路由信息)
    - 索引路由表(IndexRoutingTable,记录单个索引的所有分片信息)
    - 索引分片路由(IndexShardRoutingTable,记录一个分片的所有信息,例如主分片/副本分片)
    - 分片路由(ShardRouting,根据集群最新状态,记录此分片的路由信息)
    - 路由节点(RoutingNode,记录node上的分片信息)
### 核心接口与类
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - AbstractLifecycleComponent
            - RoutingService(监听集群状态,维护路由表更新服务类)
        - AllocationService(管理集群中节点的分片分配服务类)
        - BaseGatewayShardAllocator(分片分配器基类)
            - PrimaryShardAllocator(主分片分配器)
            - ReplicaShardAllocator(副本分片分配器)
        - AllocationDecider(分配决定器基类,决定给定的分片路由能否分配到相应的节点/保留在相应节点)
            - DiskThresholdDecider(根据磁盘空间来进行判断,是否进行分配,默认磁盘使用率高于低水位线85%新分片不分配到此节点,高于高水位线90%分片不保留在此节点)
            - FilterAllocationDecider(根据设定的index.routing.allocation策略进行分片节点的选择)
    - LifecycleComponent
- RoutingAllocation(路由分配)
- ShardsAllocator
    - BalancedShardsAllocator
- Decision(分片分配的操作标记类)
### 以 创建索引时的分片操作 为例讲解加载过程
- 索引创建参看 https://blog.csdn.net/undergrowth/article/details/82885628
    - 在创建索引的时候,先通过RoutingTable.Builder#addAsNew创建索引路由IndexRoutingTable更新RoutingTable和ClusterState
        - IndexRoutingTable.Builder#initializeAsNew/Builder#initializeEmpty根据索引的分片数目indexMetaData.getNumberOfShards()分片数目遍历创建ShardId,再创建ShardId的时候,根据索引副本数目indexMetaData.getNumberOfReplicas()创建分片的主分片和副本分片,但是这些分片都还没有路由节点
    - 然后通过allocationService.reroute根据ClusterState开启分片操作
        - 先通过最新的集群状态的RoutingTable创建RoutingNodes,保存最新的unassignedShards/assignedShards/nodesToShards信息
        - 将未分配分片shuffle,routingNodes.unassigned().shuffle()防止产生毒药后果,同时创建路由分配器RoutingAllocation
        - 然后重新路由reroute(allocation)
            - 先通过gatewayAllocator.allocateUnassigned,GatewayAllocator#allocateUnassigned/GatewayAllocator#innerAllocatedUnassigned
            利用primaryShardAllocator和replicaShardAllocator进行未分片的分配(这里区别于下面在于,网关的分片是其他目标节点上也有此分片的副本分片才可以分配)
            - 然后再shardsAllocator.allocate/BalancedShardsAllocator#allocate创建Balancer进行集群分片的分配和平衡
                - balancer.allocateUnassigned()
                - balancer.moveShards()
                - balancer.balance()
        - 最后发布集群状态,相应服务接收状态进行更改
        