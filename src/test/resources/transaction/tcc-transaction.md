# tcc-transaction 1.2.x 学习笔记
## 概述
- 参考
    - https://github.com/changmingxie/tcc-transaction/wiki/%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%971.2.x
    - https://github.com/changmingxie/tcc-transaction
## 启动
- checkout master-1.2.x
- 执行 tcc-transaction-tutorial-sample/tcc-transaction-dubbo-sample/src/main/dbscripts 创建相应的库表,CAP_TRADE_ORDER/RED_TRADE_ORDER补上version字段
- 修改tcc-transaction-dubbo-sample/tcc-transaction-sample-domain各项目相应的jdbc/zookeeper配置文件连接信息
- 分别打包,启动capital/redpacket/order服务,capital以dubbo:service方式提供资金账号服务,redpacket以dubbo:service方式提供红包账号服务,order以dubbo:reference方式消费账号与红包服务
- 通过order提供的商品列表,进行购买行为,完成tcc调用,正常日志如下
```
order-log
order try make payment called.time seq:2018-06-19 14:44:46
order confirm make payment called. time seq:2018-06-19 14:44:57

capital-log
capital try record called. time seq:2018-06-19 14:44:47
capital confirm record called. time seq:2018-06-19 14:45:08

redpacket-log
red packet try record called. time seq:2018-06-19 14:44:49
red packet confirm record called. time seq:2018-06-19 14:45:09
```
## 源码分析
### org.mengyun.tcctransaction.api.Compensable----入口
- 通过Compensable注解,传递confirmMethod、cancelMethod方法
### 切面和拦截----CompensableTransactionAspect/CompensableTransactionInterceptor
- CompensableTransactionAspect----定义切入点,只要有Compensable注解的均拦截
    - compensableTransactionInterceptor.interceptCompensableMethod(pjp)
- CompensableTransactionInterceptor----通过连接点ProceedingJoinPoint,获取目标对象/方法/参数,进行事务的begin、业务处理returnValue = pjp.proceed()、事务提交transactionManager.commit、或者是事务回滚transactionManager.rollback
- org.mengyun.tcctransaction.interceptor.ResourceCoordinatorAspect/org.mengyun.tcctransaction.interceptor.ResourceCoordinatorInterceptor
    - 通过ResourceCoordinatorInterceptor拦截器,加入事务的Participant,从而获取业务定义的confirm与cancel方法,构成confirmInvocation/cancelInvocation,进行后续的事务提交
### 事务管理----org.mengyun.tcctransaction.TransactionManager----管理事务的生命周期/异步执行/存储(委托给transactionRepository)
- 事务开始begin
- 事务提交commit
    - org.mengyun.tcctransaction.TransactionManager#commitTransaction
    - org.mengyun.tcctransaction.Participant#commit
        - 回调ResourceCoordinatorInterceptor创建的confirm,调用confirmInvocationContext,实现业务的confirm
- 事务回滚rollback
    - org.mengyun.tcctransaction.TransactionManager#rollbackTransaction
    - org.mengyun.tcctransaction.Transaction#rollback
        - org.mengyun.tcctransaction.Participant#rollback----调用cancelInvocationContext,实现业务的cancel
- 事务上下文清理cleanAfterCompletion
### 事务存储----org.mengyun.tcctransaction.TransactionRepository
- 支持jdbc/redis/zookeeper/filesystem存储事务状态
