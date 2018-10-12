# elasticsearch 5.2.2 学习笔记之源码阅读9-发现模块(DiscoveryModule)总览之主节点选举_故障检测_集群状态发布
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/modules-discovery-zen.html
    - https://blog.csdn.net/undergrowth/article/details/82851401
    - https://blog.csdn.net/undergrowth/article/details/82885628
    - https://www.jianshu.com/p/b21b42d02bd8
## 发现模块(DiscoveryModule)总览之主节点选举_故障检测_集群状态发布
- 主节点选举
    - 通过UnicastZenPing机制,找到MasterCandidate候选节点,先按照clusterStateVersion越大,就是主节点,版本一致再按照主节点优先,最后才是根据节点id的升序选出主节点
- 故障检测
    - MasterFaultDetection和NodesFaultDetection都是根据ping机制,去核查节点是否存活
- 集群状态发布
    - 集群的状态发布采用两步完成,第一步主节点先发布状态给到各节点,当超过minMasterNodes响应后,再给各节点发送提交状态请求,各节点接收到请求后进行节点状态的更新
### 核心接口与类
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - AbstractLifecycleComponent
            - ZenDiscovery(zen发现机制的实现类,为发现模块默认的实现,主要操作均在此类,例如主节点选举/故障状态监测发起/集群状态的发布与接收处理)
        - MembershipAction(节点加入集群/离开集群等相关操作)
            - DISCOVERY_JOIN_ACTION_NAME = "internal:discovery/zen/join" 以及相关请求 JoinRequestRequestHandler处理类
            - DISCOVERY_JOIN_VALIDATE_ACTION_NAME = "internal:discovery/zen/join/validate" 以及相关请求 ValidateJoinRequestRequestHandler处理类
            - DISCOVERY_LEAVE_ACTION_NAME = "internal:discovery/zen/leave" 以及相关请求 LeaveRequestRequestHandler处理类
        - FaultDetection(故障检测基类)
            - MasterFaultDetection(其他节点ping主节点,查看其是否存活)
            - NodesFaultDetection(主节点ping其他节点,查看集群中节点是否存活)
         - PublishClusterStateAction(发布集群状态组件)
            - 状态两步操作
                - SEND_ACTION_NAME = "internal:discovery/zen/publish/send" 以及相关请求 SendClusterStateRequestHandler处理类
                - COMMIT_ACTION_NAME = "internal:discovery/zen/publish/commit" 以及相关请求 CommitClusterStateRequestHandler处理类
         - ElectMasterService(主节点选举服务类,负责主节点选举)
         - NodeJoinController(处理MembershipAction操作)
    - LifecycleComponent
        - Discovery(发现机制的接口,负责发现其他节点/发布集群状态/主节点选举)
            - Discovery#publish
            - Discovery#startInitialJoin
- DiscoveryNode(表示发现模块的节点封装)
- ZenPing(ping机制接口)
    - UnicastZenPing(ping的实现类,所有跟ping操作相关的类均委托给此类)
- DiscoveryModule(发现模块,加载相关支持类给到集群,例如加载ZenDiscovery)
- PendingClusterStatesQueue(存放集群状态的队列,配合集群状态发布)
- JoinThreadControl(线程同步工具,用于同步选举过程)
### 以 主节点选举 为例讲解加载过程
### 以 故障检测 为例讲解加载过程
### 以 集群状态发布 为例讲解加载过程
    


        
