# spring-cloud-eureka-server之spring-cloud-1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
## 概述
- 利用EnableEurekaServer注解启用EurekaServerAutoConfiguration配置,
- EurekaServerAutoConfiguration利用配置生成功能bean注入beanfactory,EurekaServerInitializerConfiguration利用EurekaServerBootstrap启动注册中心相关服务
- 参考
    - http://blog.csdn.net/undergrowth/article/details/77620064
## EnableEurekaServer----->启用注册中心相关的配置与服务
- 使能EnableDiscoveryClient---->Annotation to enable a DiscoveryClient implementation
    - 启用服务提供者与服务消费者(对于eureka注册中心而言,提供者与消费者都是DiscoveryClient/EurekaClient)
    - 详细解析参见spring-cloud-eureka-server之spring-cloud-1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
- 引入EurekaServerMarkerConfiguration的Marker开启EurekaServerAutoConfiguration开关
## EurekaServerAutoConfiguration---->用于EurekaServer往beanfactory添加相关eureka-server功能bean
- 因为启用EnableDiscoveryClient,即在解析EnableDiscoveryClient时,就有ApplicationInfoManager/EurekaClientConfig/EurekaClient的bean支持了
- 注入EurekaServerConfig---->用于注册中心相关配置信息
- 注入EurekaController---->提供注册中心上相关服务信息的展示支持
- 注入PeerAwareInstanceRegistry---->提供实例注册支持,例如实例获取、状态更新等相关支持
- 注入PeerEurekaNodes---->提供注册中心对等服务间通信支持
- 注入EurekaServerContext---->提供初始化注册init服务、初始化PeerEurekaNode节点信息
    - 在eureka级别,DefaultEurekaServerContext为入口
- 注入EurekaServerBootstrap---->用于初始化initEurekaEnvironment/initEurekaServerContext
## EurekaServerBootstrap---->用于初始化initEurekaEnvironment/initEurekaServerContext
- initEurekaServerContext---->用于通过openForTraffic改变实例状态为InstanceStatus.UP,启动EvictionTask任务,移除过期的实例信息(默认为60s)
## EurekaServerInitializerConfiguration---->引入SmartLifecycle,在应用启动时,调用EurekaServerBootstrap完成注册中心的相关初始化操作
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git