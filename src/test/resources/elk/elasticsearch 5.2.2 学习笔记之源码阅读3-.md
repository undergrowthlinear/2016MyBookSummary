# elasticsearch 5.2.2 学习笔记之源码阅读3-TransportService数据传输
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
## 接收请求与处理流程
```
TransportService(利用Transport进行数据传输,使用netty作为数据传输层)
```
### 核心接口与类
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
- Releasable(Closeable)
    - LifecycleComponent(start/stop/addLifecycleListener)
        - Transport
            - TcpTransport
                - Netty4Transport
            - LocalTransport
        - AbstractLifecycleComponent
            - TransportService
### 查询集群状态详细代码流
    


        
