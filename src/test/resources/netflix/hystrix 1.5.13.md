# hystrix 1.5.13 学习笔记
## 概述
- 参考
    - https://segmentfault.com/a/1190000005988895
    - http://www.infoq.com/cn/news/2013/01/netflix-hystrix-fault-tolerance
    - http://www.sczyh30.com/posts/Microservice/netflix-hystrix-how-it-works-summary/
    - 用舱壁隔离的方式进行资源隔离,利用熔断器进行链路的保护与恢复
## HystrixThreadPool---->资源隔离
- HystrixThreadPoolDefault---->HystrixThreadPool
## HystrixCircuitBreaker---->熔断器
- HystrixCircuitBreakerImpl---->HystrixCircuitBreaker
  - com.netflix.hystrix.HystrixCircuitBreaker.HystrixCircuitBreakerImpl#attemptExecution---->命令根据此方法判断是否应该执行
    - 基于circuitBreakerForceOpen(false)/default_circuitBreakerForceClosed(false)
    - circuitOpened---->熔断打开时间(-1)
    - isAfterSleepWindow---->是否是在熔断的休眠时间后,基于currentTime > circuitOpenTime + sleepWindowTime(circuitBreakerSleepWindowInMilliseconds----5000ms)
    - 置熔断状态为半开状态status.compareAndSet(Status.OPEN, Status.HALF_OPEN)
  - com.netflix.hystrix.HystrixCircuitBreaker.HystrixCircuitBreakerImpl#subscribeToStream
    - 当达到设定的阀值时,status.compareAndSet(Status.CLOSED, Status.OPEN)
- NoOpCircuitBreaker---->HystrixCircuitBreaker----->关闭熔断支持
- 熔断器三种状态---->CLOSED, OPEN, HALF_OPEN
## HystrixCommand---->命令模式
- HystrixCommand---->AbstractCommand
- terminateCommandCleanup---->完成清理动作
- unsubscribeCommandCleanup---->完成从status.compareAndSet(Status.HALF_OPEN, Status.OPEN)的转化
- com.netflix.hystrix.AbstractCommand#applyHystrixSemantics---->核心流程方法
  - com.netflix.hystrix.AbstractCommand#executeCommandAndObserve
  - com.netflix.hystrix.AbstractCommand#executeCommandWithSpecifiedIsolation
  - 正常回调
    - com.netflix.hystrix.AbstractCommand#getUserExecutionObservable---->调用HystrixCommand的方法,进行回调业务系统
    - com.netflix.hystrix.HystrixCommand#getExecutionObservable---->回调用户定义的业务run方法
  - 异常回调
  - com.netflix.hystrix.AbstractCommand#handleShortCircuitViaFallback
  - com.netflix.hystrix.AbstractCommand#getFallbackOrThrowException
  - com.netflix.hystrix.HystrixCommand#getFallbackObservable---->回调用户定义的业务异常getFallback方法
## HystrixKeyDefault---->HystrixKey---->key支持(String name)
- HystrixThreadPoolKey---->利用intern存放以name为key,HystrixThreadPoolKeyDefault为值的Map
- HystrixCommandKey---->利用intern存放以name为key,HystrixCommandKeyDefault为值的Map
- HystrixCommandGroupKey---->利用intern存放以name为key,HystrixCommandGroupDefault为值的Map
- HystrixCollapserKey---->利用intern存放以name为key,HystrixCollapserKeyDefault为值的Map
## 属性支持---->例如命令属性、线程池属性
- HystrixThreadPoolProperties
- HystrixCommandProperties
- HystrixCollapserProperties
## HystrixMetrics---->例如指标信息
- HystrixThreadPoolMetrics---->HystrixMetrics---->指标信息
- HystrixCommandMetrics---->HystrixMetrics
- HystrixCollapserMetrics---->HystrixMetrics
## 测试
- com.netflix.hystrix.examples.demo.HystrixCommandDemo