# kafka-clients 1.1.0 生产者与消费者源码简析
## 概述
- 参考
## 生产者
### 第一步----将要发送的数据放入bytestream
```
org.apache.kafka.clients.producer.KafkaProducer#KafkaProducer
org.apache.kafka.clients.producer.ProducerRecord#ProducerRecord
org.apache.kafka.clients.producer.KafkaProducer#send
    this.interceptors.onSend
org.apache.kafka.clients.producer.KafkaProducer#doSend
    keySerializer.serialize
    valueSerializer.serialize
    tp = new TopicPartition
org.apache.kafka.clients.producer.internals.RecordAccumulator#append
org.apache.kafka.clients.producer.internals.RecordAccumulator#tryAppend
    ConcurrentMap<TopicPartition, Deque<ProducerBatch>> batches
org.apache.kafka.clients.producer.internals.ProducerBatch#tryAppend
org.apache.kafka.common.record.MemoryRecordsBuilder#appendWithOffset
org.apache.kafka.common.utils.ByteBufferOutputStream----将数据写入包装的ByteBuffer
    result.batchIsFull || result.newBatchCreated----根据条件
    this.sender.wakeup----唤醒发送者线程,以nio的方式将数据写入channel
```
### 第二步----激活通道的写事件,将数据绑定在通道上
```
org.apache.kafka.clients.producer.internals.Sender#run(long)
org.apache.kafka.clients.producer.internals.Sender#sendProducerData----激活通道
org.apache.kafka.clients.producer.internals.RecordAccumulator#drain
org.apache.kafka.clients.producer.internals.Sender#sendProduceRequests
org.apache.kafka.clients.producer.internals.Sender#sendProduceRequest
    clientRequest = client.newClientRequest
org.apache.kafka.clients.NetworkClient#doSend
org.apache.kafka.common.network.Selector#send
org.apache.kafka.common.network.KafkaChannel#setSend
    this.send = send
    this.transportLayer.addInterestOps(SelectionKey.OP_WRITE)----添加写事件----激活通道
```
### 第三步----利用nio获取激活的事件,进行实际的socket操作
```
org.apache.kafka.clients.NetworkClient#poll----实际发送socket
org.apache.kafka.common.network.Selector#poll
org.apache.kafka.common.network.Selector#select
    this.nioSelector.select----获取之前通道的事件
org.apache.kafka.common.network.Selector#pollSelectionKeys
org.apache.kafka.common.network.Selector#channel
org.apache.kafka.common.network.KafkaChannel#write
org.apache.kafka.common.network.KafkaChannel#send
org.apache.kafka.common.network.ByteBufferSend#writeTo
```
## 消费者
```
org.apache.kafka.clients.consumer.KafkaConsumer#KafkaConsumer
org.apache.kafka.clients.consumer.KafkaConsumer#subscribe----绑定topic信息
org.apache.kafka.clients.consumer.internals.SubscriptionState#subscribe
org.apache.kafka.clients.consumer.KafkaConsumer#poll
org.apache.kafka.clients.consumer.KafkaConsumer#pollOnce
org.apache.kafka.clients.consumer.internals.Fetcher#sendFetches----当无数据时,发起获取数据的请求
org.apache.kafka.clients.consumer.internals.RequestFutureListener#onSuccess
    completedFetches.add(new CompletedFetch())----获取数据成功的回调
org.apache.kafka.clients.consumer.internals.Fetcher#fetchedRecords----解析获取到的数据
org.apache.kafka.clients.consumer.internals.Fetcher#parseCompletedFetch
org.apache.kafka.clients.consumer.internals.Fetcher#fetchRecords
org.apache.kafka.clients.consumer.internals.Fetcher.PartitionRecords#fetchRecords
    records.add(parseRecord())
```
## 示例代码
- https://github.com/undergrowthlinear/bigdata-learn/blob/dev/example-hdfs-learn/src/main/java/com/undergrowth/kafka/ConsumerDemo.java
- https://github.com/undergrowthlinear/bigdata-learn/blob/dev/example-hdfs-learn/src/main/java/com/undergrowth/kafka/ProducerDemo.java