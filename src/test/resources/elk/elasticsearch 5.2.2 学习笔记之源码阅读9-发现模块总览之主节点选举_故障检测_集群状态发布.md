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
                - MASTER_PING_ACTION_NAME = "internal:discovery/zen/fd/master_ping" 以及相关请求 MasterPingRequestHandler处理类
            - NodesFaultDetection(主节点ping其他节点,查看集群中节点是否存活)
                - PING_ACTION_NAME = "internal:discovery/zen/fd/ping" 以及相关请求 PingRequestHandler处理类
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
- node启动查看  https://blog.csdn.net/undergrowth/article/details/82840411
    - 在node创建时,通过DiscoveryModule模块注入默认的ZenDiscovery,在Node#start的时候调用discovery.startInitialJoin开始同步主节点选举过程
        - 使用JoinThreadControl#startNewThreadIfNotRunning/ZenDiscovery#innerJoinCluster开始线程间同步,
        ZenDiscovery#findMaster开始进行主节点的选举
            - 第一步,通过pingAndWait(pingTimeout)找到可以ping通的节点信息
                - UnicastZenPing#ping通过resolveHostsLists解析配置的host,构造PingingRound后,通过发送ACTION_NAME = 
                "internal:discovery/zen/unicast"请求给对应节点
                - 再看对应节点ACTION_NAME的UnicastPingRequestHandler,
                UnicastZenPing#handlePingRequest响应PingResponse包含本地节点信息/主节点信息/集群状态信息
            - 第二步,进行必要的过滤/转换后,形成List<ElectMasterService.MasterCandidate>
            - 第三步,通过electMaster.electMaster进行选举主节点
                - 以ElectMasterService.MasterCandidate#compare提供的机制排序(先按照clusterStateVersion越大,就是主节点,版本一致再按照主节点优先,最后才是根据节点id的升序选出主节点)候选节点,候选节点第一个为主节点
        - 找到主节点后,如果主节点是本地节点,则开启节点的nodesFD.updateNodesAndPing故障检测机制,即回调NodesFaultDetection.NodeFD#run利用ping机制进行检测
            -  如果自己不是主节点,则ZenDiscovery#joinElectedMaster发起请求,加入主节点,即通过membership.sendJoinRequestBlocking发送DISCOVERY_JOIN_ACTION_NAME的请求
                - 对于主节点接收DISCOVERY_JOIN_ACTION_NAME请求会回调JoinRequestRequestHandler进行相关处理,
                调用NodeJoinController#handleJoinRequest进行相关集群状态的变更,处理流程跟上篇GatewayModule处理流程类似
### 以 故障检测 为例讲解加载过程
- MasterFaultDetection(其他节点ping主节点,查看其是否存活)与NodesFaultDetection(主节点ping其他节点,查看集群中节点是否存活)都是内嵌在ZenDiscovery模块中,
利用ping机制,原理与上面类似,通过PING_ACTION_NAME与相应的处理器PingRequestHandler进行扭转
### 以 集群状态发布 为例讲解加载过程
- 在 https://blog.csdn.net/undergrowth/article/details/82885628 创建索引时,提到过当创建索引后,会创建UpdateTask进行集群状态的发布,现在来详细看看
    - org.elasticsearch.cluster.service.ClusterService.UpdateTask#run构建TaskInputs调用ClusterService#runTasks
        - 第一步,通过calculateTaskOutputs获得TaskOutputs
            - 执行之前定义任务executeTasks
            - 通过patchVersionsAndNoMasterBlocks获得新的集群状态
        - 第二步,集群状态是否改变,如改变,则通过publishAndApplyChanges发布ClusterChangedEvent事件
            - 通过集群新旧状态构建ClusterChangedEvent事件
            - 确认各节点连接有效nodeConnectionsService.connectToNodes
            - 发布状态clusterStatePublisher.accept,即回调在node.start时传入的clusterService.setClusterStatePublisher
            (discovery::publish),即ZenDiscovery#publish,调用PublishClusterStateAction#publish正式开始集群状态的两阶段提交
        - 第三步,集群状态的两阶段提交(PublishClusterStateAction#innerPublish)(消息在如下流程扭转,各节点确认后,状态更新跟之前一致)
            - SEND_ACTION_NAME = "internal:discovery/zen/publish/send" 以及相关请求 SendClusterStateRequestHandler处理类
            - COMMIT_ACTION_NAME = "internal:discovery/zen/publish/commit" 以及相关请求 CommitClusterStateRequestHandler处理类
    


        
