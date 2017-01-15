##2016书单总结--从Paxos到Zookeeper分布式一致性原理与实践--原理篇
1. 集中式--由一台或者多台计算机组成中心节点，由中心节点进行数据存储和业务单元处理
    - 结构简单
    - 去IOE(IBM小型机 Oracle数据库 Emc存储设备)
2. 分布式--由一个软件或者硬件分布在不同的网络计算机上，彼此之间通过消息进行通信和协调
    - 分布式--空间上随意分布
    - 对等性--无主从之分
    - 并发性--并发计算/存储
    - 缺乏系统时钟
    - 故障总是会发生
        * 通信异常
        * 网络分区--脑裂(局部组成小网络)
        * 三态--响应分为成功、失败、超时
        * 节点故障
3. 事务(对数据的访问与更新处于一个执行逻辑单元)处理与数据一致性
    分布式事务--事务的参与者，支持事务的服务器、资源服务器以及事务管理器分别位于分布式系统的不同节点上
    - ACID
        * A(atomicity)--原子性(事务要么全部执行成功，要么全部执行失败)
        * C(consistency)--一致性(事务执行使数据库从一个一致性状态转为另一个一致性状态)
        * I(isolation)--隔离性(并发环境中，事务之间的操作是相互隔离的)
            * 未授权读/读未提交(ReadUncommited)--存在脏数据
            * 授权读取(ReadCommited)--3个事务操作/存在不可重复读取
            * 可重复读(RepeatableRead)--同一事务中，可重复读/存在幻读(同样的事务操作，前后时间段读取数据不一致)
            * 串行化(Serializable)--事务串行执行
        * D(durability)--持久性(事务一旦提交，对数据库的修改就是永久性的)
    - CAP--一个分布式系统不可能同时满足CAP，最多只能满足其中两项
        * C(consistency/一致性)--数据在多个副本之间是否能够保持一致
        * A(availability/可用性)--服务在指定时间内返回结果
        * P(partition tolerance/分区容错性)--在遇到网络分区故障时，需要保障对外提供一致性和可用性的服务
    - BASE--对CAP的一致性与可用性进行权衡的结果
        * BA(basic available/基本可用)--遭遇故障时，允许损失部分可用性
        * S(soft state/弱状态)--允许数据出现中间状态
        * E(eventually consistent/最终一致性)--所有数据副本，经过一段时间同步，最终达到数据的一致性
            * 因果一致性(Causal)
            * 读己之所写(Read your writes)
            * 会话一致性(Session)
            * 单调读一致性(Monotomic read)
            * 单调写一致性(Monotonic write)
4. 一致性协议
协调者(coordinator)负责调度参与者(participant)的行为，并最终决定是否要把参与者进行的事务进行提交
    - 2PC(two phase commit/两阶段提交)--投票与执行
        * 提交事务请求(投票)--协调者发送事务请求，参与者执行，返回结果
        * 执行事务提交(执行)--协调者根据参与者返回的信息，决定是否提交事务或者回滚事务
        * 优缺点
            * 优点--原理简单，实现方便
            * 缺点--参与者同步阻塞、协调者单点问题、参与者数据不一致、协调者太过保守
    - 3PC(three-phase-commit/三阶段提交)--降低参与者阻塞的范围
        * CanCommit--协调者询问参与者是否可进行事务提交，参与者根据情况评估后返回结果
        * PreCommit--协调者根据参与者返回情况，发送执行事务请求或者中断事务请求，等待参与者结果
        * doCommit--协调者根据参与者执行情况，发送提交事务或者回滚事务请求
        * 优缺点
            * 优点--CanCommit可降低参与者阻塞的时间
            * 缺点--参与者会出现数据的不一致
    - Paxos--分布式系统容错性的一致性算法
        * 保障最终有一个提案被选定，当提案被选定后，进程最终也能获取到被选定的提案
        * proposer/acceptor/learner
5. ZAB--Zookeeper Atomic Broadcast协议--支持崩溃恢复的原子广播协议
    - client-->leader--proposal<-->follower1
                               <-->follower2
                               <-->follower3
                               <-->follower4
                       (过半)commit<-->follower1
                             <-->follower2
                             <-->follower3
                             <-->follower4
    - 消息广播--原子广播协议，类似于二阶段提交
    - 崩溃恢复--选出新的leader
    - Leader(为客户端提供读和写服务)
    - Follower(提供读服务/参与过半写成功策略/参与选举Leader)
    - Observer(提供读服务/不参与过半写成功策略/不参与选择Leader)
6. 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git
    * 使用curator创建模拟zookeeper服务集群在com.undergrowth.zookeeper.paxos.test.CuratorZookeeperServerTest代码示例
    * 使用curator操作zookeeper在com.undergrowth.zookeeper.paxos.test.CuratorLearnTest代码演示
        * 包括创建节点、删除节点、获取节点数据、设置节点数据、监听节点数据改变、监听子节点改变
        * 分布式计数器、分布式锁
    * 使用zk操作zookeeper在com.undergrowth.zookeeper.paxos.test.zk.ZooKeeperOperator代码演示