# spring-cloud-hystrix之spring-cloud-1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
## 概述
- hystrix可单独使用,也可与feign搭配使用,详见HystrixTargeter
- 参考
      - https://fangjian0423.github.io/2017/02/19/springcloud-hystrix/
      - http://blog.csdn.net/undergrowth/article/details/78013021
      - http://blog.csdn.net/undergrowth/article/details/77859293
## EnableCircuitBreaker---->开启hystrix配置
## HystrixCircuitBreakerConfiguration---->配置hystrix切面,监控信息
- 注入HystrixCommandAspect,使用切面处理HystrixCommand注解标记
    - 委托CommandExecutor#execute执行HystrixCommand#execute命令
- 注入HystrixWebConfiguration与HystrixMetricsPollerConfiguration,启用web的hystrix信息的展示
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git