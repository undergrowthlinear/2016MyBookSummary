# spring-cloud-feign之spring-cloud-1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
## 概述
- 利用EnableFeignClients注解引入FeignClientsRegistrar,FeignClientsRegistrar解析EnableFeignClients与FeignClient注解信息,
利用FeignClientFactoryBean创建FeignClient标记接口的代理类(以serviceId+FeignClient为alias)
- FeignAutoConfiguration提供feign级别相应的bean支持,FeignRibbonClientAutoConfiguration提供ribbon-feign级别的bean支持
- 参考
      - http://blog.csdn.net/undergrowth/article/details/77625767
      - http://blog.csdn.net/undergrowth/article/details/77800470
      - http://blog.csdn.net/undergrowth/article/details/77859293
## EnableFeignClients---->引入FeignClientsRegistrar,启用feign相关功能
## FeignClientsRegistrar---->解析EnableFeignClients与FeignClient注解信息,注入相关的bean到beanfactory
- registerDefaultConfiguration---->注册EnableFeignClients上的defaultConfiguration配置
- registerFeignClients---->解析FeignClient注解
    - registerFeignClient---->利用FeignClientFactoryBean创建feignclient标记的接口的代理实现类
## FeignClient---->用于标记接口,表示接口的rest代理类需要创建
- name/value/url/decode404/configuration/fallback/fallbackFactory
- type为接口的class信息(在创建接口的代理类时需要使用)
## FeignAutoConfiguration
- 注入FeignContext,用于存放相应的bean信息
- Targeter---->用于是否使用hystrix作为熔断
    - 当feign.hystrix.HystrixFeign类存在时,注入HystrixTargeter,否则为DefaultTargeter
- 当无ribbon支持时,即com.netflix.loadbalancer.ILoadBalancer不存在类路径,创建无负载均衡的Client(要么是ApacheHttpClient或者OkHttpClient)
## FeignRibbonClientAutoConfiguration---->当ILoadBalancer存在时,加入ribbon支持feign
- 当beanfactory无Client时,创建LoadBalancerFeignClient,至于是ApacheHttpClient或者OkHttpClient,取决于类路径下类是否存在
- 注入CachingSpringLoadBalancerFactory,支持负载均衡的缓存
## FeignClientsConfiguration---->注入feign级别相关的编解码器、日志工厂、feign生成器
- Decoder/Encoder/Contract/Feign.Builder/Retryer/FeignLoggerFactory
## FeignClientFactoryBean---->根据feignclient解析的信息,以及FeignContext中拥有的bean信息,创建feign代理实现类
- name/type/fallback/fallbackFactory
- getObject---->获取代理实现类对象
    - feign---->根据上面配置器配置的bean,从feigncontext中创建Feign.Builder
    - 如果feignclient注解没有配置url参数,则使用loadBalance,创建LoadBalancerFeignClient进行ribbon支持的feign
      - 如果有设置,则使用LoadBalancerFeignClient中实际的要么是ApacheHttpClient或者OkHttpClient进行feign的操作
    - 后续HystrixTargeter.target,如果设置fallback,则targetWithFallback使用HystrixFeign.target创建HystrixInvocationHandler处理器中转处理,
    - 创建feign的hystrixCommand命令完成hystrix支持的feign
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git