# spring-cloud-eureka-client之spring-cloud-1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
## 概述
- 利用EnableEurekaClient注解启用EurekaClientAutoConfiguration配置,EurekaClientAutoConfiguration启用相关bean,
- EurekaAutoServiceRegistration在SmartLifecycle周期内,利用EurekaServiceRegistry往注册中心注册服务
- 参考
    - http://blog.csdn.net/undergrowth/article/details/77620064
## EnableEurekaClient----->启用服务提供者与服务消费者
- 使能EnableDiscoveryClient---->Annotation to enable a DiscoveryClient implementation
  - 启用服务提供者与服务消费者(对于eureka注册中心而言,提供者与消费者都是DiscoveryClient/EurekaClient)
  - EnableDiscoveryClient引入AutoServiceRegistrationConfiguration支持,用于服务提供者与服务消费者往注册中心注册服务
## EurekaClientAutoConfiguration---->用于EurekaClient往beanfactory添加相关eureka-client功能bean
- 通过EurekaClientConfigBean与EurekaDiscoveryClientConfiguration(eureka.client.enabled是否启用)启用EurekaClientAutoConfiguration相关bean
- 注入eurekaInstanceConfigBean(eureka.instance.)---->用于获取当前服务实例信息
  - 例如hostname/preferIpAddress/server.port/scheme/management.port
  - 获取instanceId---->getDefaultInstanceId---->默认情况为---->spring.application.name:server.port
- 注入EurekaServiceRegistry---->用于eureka的服务注册
- 注入EurekaRegistration注册信息,取决于EnableDiscoveryClient引入的AutoServiceRegistrationConfiguration已存在于beanfactory
- 注入EurekaAutoServiceRegistration---->引入spring的生命周期,启用服务注册功能,利用EurekaServiceRegistry注册EurekaRegistration信息
- 注入CloudEurekaClient---->与eureka-server操作的核心类,属于eureka级别,与spring无关系,详情见参考
  - CloudEurekaClient继承DiscoveryClient,创建时即会启用CacheRefreshThread线程,从注册中心获取服务信息
  - 创建HeartbeatThread线程,用于向注册中心发送服务续约心跳,第一次发送心跳时,返回404,即调用register,注册服务
- 注入ApplicationInfoManager---->用于管理应用信息,并且是RefreshScope与Lazy的
## EurekaAutoServiceRegistration---->引入spring的生命周期,启用服务注册功能,利用EurekaServiceRegistry注册EurekaRegistration信息
## EurekaServiceRegistry---->用于eureka的服务注册
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git