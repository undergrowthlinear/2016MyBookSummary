# flume 1.8.0 源码简析
## 概述
- 参考
- https://www.jianshu.com/p/0187459831af
- https://blog.csdn.net/anjing900812/article/details/48999067
```
基础类----org.apache.flume.lifecycle.LifecycleAware----生命周期管理
```
## 代码流
### 第一步----启动与配置
```
eg: /usr/local/bigdata/flume/bin/flume-ng agent --conf conf --conf-file /usr/local/bigdata/flume/conf/test-flume-kafka-to-hdfs.conf --name agent &
org.apache.flume.node.Application#main----agent启动入口
org.apache.flume.node.AbstractConfigurationProvider#getConfiguration
org.apache.flume.node.PropertiesFileConfigurationProvider#getFlumeConfiguration----解析传递的配置文件
    org.apache.flume.conf.FlumeConfiguration#addRawProperty
    agentConfigMap.put(agentName, aconf)----将属性信息加入到AgentConfiguration
org.apache.flume.node.AbstractConfigurationProvider#loadChannels----加载channel
org.apache.flume.node.AbstractConfigurationProvider#loadSources
    ChannelSelector selector----加入选择器,默认为ReplicatingChannelSelector(复制选择器)
    ChannelProcessor channelProcessor----加入通道处理器(source消息扭转)
    SourceRunner.forSource----加入source的启动器
org.apache.flume.node.AbstractConfigurationProvider#loadSinks
    加入配置的sink
org.apache.flume.node.AbstractConfigurationProvider#getConfiguration
    conf.addSourceRunner----加入source启动器,用于启动source流程
    conf.addSinkRunner----加入sink启动器,用于启动sink流程
org.apache.flume.node.Application#handleConfigurationEvent
org.apache.flume.node.Application#startAllComponents----启动通道/source启动器/sink启动器
    org.apache.flume.lifecycle.LifecycleSupervisor#supervise
    org.apache.flume.lifecycle.LifecycleSupervisor.MonitorRunnable#run
    lifecycleAware.start()----分别启动source流程与sink流程
```
### 第二步----Source处理流程(利用source启动器驱动source流程处理/以KafkaSource为例)
```
org.apache.flume.source.PollableSourceRunner#start----在配置完后,启动source启动器
    org.apache.flume.source.BasicSourceSemantics#start----初始化
    org.apache.flume.source.kafka.KafkaSource#doStart----以kafka为例
        consumer = new KafkaConsumer----kafka的消费者配置和订阅相关主题
        subscriber.subscribe
org.apache.flume.source.PollableSourceRunner.PollingRunner#run
org.apache.flume.source.AbstractPollableSource#process
org.apache.flume.source.kafka.KafkaSource#doProcess
    consumer.poll----从kafka获取消息
    event = EventBuilder.withBody(eventBody, headers)----将获取的body/header封装成event(后续的处理基础)
    getChannelProcessor().processEventBatch----利用channelProcessor处理封装好的eventList
org.apache.flume.channel.ChannelProcessor#processEventBatch
    interceptorChain.intercept(events)----一系列的拦截器处理
    selector.getRequiredChannels----通过选择器根据事件选择关注事件的通道
    org.apache.flume.channel.MultiplexingChannelSelector#getRequiredChannels----多路复用的根据headerName的值选择相应的通道
    eventQueue.add(event)----将事件添加到相应通道的event队列
    reqChannel.put
org.apache.flume.channel.BasicTransactionSemantics#commit
org.apache.flume.channel.MemoryChannel.MemoryTransaction#doCommit----以memory为例,在event提交时将其加入到queue
    LinkedBlockingDeque<Event> queue----后续sink即是从其中获取消息
```
### 第三步----Sink处理流程(利用sink启动器驱动sink流程处理)----流程比source简单
```
org.apache.flume.SinkRunner#start----初始化
org.apache.flume.sink.DefaultSinkProcessor#start----默认的sinkProcessor处理
org.apache.flume.sink.hdfs.HDFSEventSink#start----以hdfs sink为例
org.apache.flume.SinkRunner.PollingRunner#run----消费事件
org.apache.flume.sink.DefaultSinkProcessor#process
org.apache.flume.sink.hdfs.HDFSEventSink#process
    channel.take()
    org.apache.flume.channel.BasicChannelSemantics#take
    org.apache.flume.channel.MemoryChannel.MemoryTransaction#doTake----以memory通道为例
    event = queue.poll()----从source放入的queue获取event进行后续的写入hdfs
```
## 示例配置
```
#define name
agent.sources = kafkaSource
agent.channels = cbaidu cgoogle
agent.sinks = hdfsSink1 lg1
#kafkaSource
agent.sources.kafkaSource.type=org.apache.flume.source.kafka.KafkaSource
agent.sources.kafkaSource.zookeeperConnect=s3.ds.tc:2181,s4.ds.tc:2181,s5.ds.tc:2181
agent.sources.kafkaSource.topic=test
agent.sources.kafkaSource.groupId=test1
#agent.sources.kafkaSource.kafka.consumer.timeout.ms=100
#
agent.sources.kafkaSource.interceptors=i1
agent.sources.kafkaSource.interceptors.i1.type=regex_extractor
agent.sources.kafkaSource.interceptors.i1.regex=YM:(\\w+)
agent.sources.kafkaSource.interceptors.i1.serializers=s1
agent.sources.kafkaSource.interceptors.i1.serializers.s1.name = domain_name
#agent.sources.kafkaSource.interceptors.i1.serializers.s1.type = default
#
agent.sources.kafkaSource.selector.type = multiplexing
agent.sources.kafkaSource.selector.header = domain_name
agent.sources.kafkaSource.selector.mapping.baidu = cbaidu
agent.sources.kafkaSource.selector.mapping.default=cgoogle
#channel
agent.channels.cbaidu.type=memory
agent.channels.cbaidu.capacity=100000
agent.channels.cbaidu.transactionCapacity=10000
agent.channels.cgoogle.type=memory
agent.channels.cgoogle.capacity=100000
agent.channels.cgoogle.transactionCapacity=10000
#hdfssink1
agent.sinks.hdfsSink1.type=hdfs
agent.sinks.hdfsSink1.hdfs.path=hdfs://s1.ds.tc:9000/test/Hadoop/Input/test/%y-%m-%d/%H
agent.sinks.hdfsSink1.hdfs.filePrefix=test-
agent.sinks.hdfsSink1.hdfs.writeFormat=Text
agent.sinks.hdfsSink1.hdfs.fileType=DataStream
agent.sinks.hdfsSink1.hdfs.rollSize=1024000
agent.sinks.hdfsSink1.hdfs.rollCount=0
agent.sinks.lg1.type=logger
# bind source,sink to channels
agent.sources.kafkaSource.channels=cbaidu cgoogle
agent.sinks.hdfsSink1.channel = cbaidu
agent.sinks.lg1.channel=cgoogle
#agent.sources.kafkaSource.channels = memoryChannel1
```