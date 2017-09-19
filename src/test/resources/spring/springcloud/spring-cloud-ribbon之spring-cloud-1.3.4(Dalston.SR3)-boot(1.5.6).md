# spring-cloud-ribbon之spring-cloud-1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
## 概述
- 软负载均衡需要支持三个功能,带选择的服务器列表/从待选服务器中选出一个服务器/调用选择的服务器
- RibbonClients/RibbonClient引入RibbonClientConfigurationRegistrar,解析相关配置信息
- RibbonClientConfiguration注入ribbon级别支持bean(ILoadBalancer/IRule/ServerList)
- RibbonAutoConfiguration通过LoadBalancerAutoConfiguration开启负载均衡配置
- ribbon即可单独与RestTemplate联合使用,也可与feign一起连用(见上篇文章)
- 参考
      - http://blog.csdn.net/undergrowth/article/details/77800470
      - http://blog.csdn.net/undergrowth/article/details/78013021
## RibbonClients/RibbonClient---->引入RibbonClientConfigurationRegistrar,解析相关配置信息
## RibbonClientConfiguration---->注入ribbon级别支持bean(ILoadBalancer/IRule/ServerList)
- 注入ZoneAwareLoadBalancer(加入区域信息),底层依赖BaseLoadBalancer进行服务获取
- 注入ZoneAvoidanceRule,底层依赖RoundRobinRule进行轮询获取服务列表
- 注入ConfigurationBasedServerList,从配置属性获取服务列表
## RibbonAutoConfiguration---->依赖classpath路径下有IClient/RestTemplate/Ribbon开启负载注入
- 引入LoadBalancerAutoConfiguration,注入LoadBalanced注解标记的RestTemplate,给RestTemplate添加LoadBalancerInterceptor或者RetryLoadBalancerInterceptor拦截器
- 注入SpringClientFactory
- 注入LoadBalancerClient(RibbonLoadBalancerClient)---->支撑ribbon进行轮询调用,与feign结合使用一致,只不过开启的开关不一样
## LoadBalancerAutoConfiguration---->负载均衡支持
- restTemplates---->注入应用配置的LoadBalanced注解标记的RestTemplate
- 注入LoadBalancerRequestFactory,负载的请求创建工厂
- 如果无RetryTemplate,则将LoadBalancerInterceptor添加至RestTemplate拦截器
- 如果有RetryTemplate,则使用RetryLoadBalancerInterceptor添加至RestTemplate拦截器
## LoadBalancerInterceptor---->request拦截器,利用RibbonLoadBalancerClient进行服务的选择,请求的执行
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git