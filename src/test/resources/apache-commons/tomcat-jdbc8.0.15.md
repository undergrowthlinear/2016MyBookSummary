# tomcat-jdbc8.0.15 学习笔记
## 概述
- 参考
  - http://blog.csdn.net/white_ice/article/details/52610136
  - http://tomcat.apache.org/tomcat-8.0-doc/jdbc-pool.html
  - http://blog.csdn.net/undergrowth/article/details/77543528
## DataSource---->数据源
- DataSource---->DataSourceProxy---->DataSource(支持创建基本连接)---->ConnectionPoolDataSource(支持创建池连接)
- XADataSource---->DataSource---->XADataSource(支持池连接与分布式事务管理)
- DataSource---->入口(继承DataSourceProxy,连接的管理委托给ConnectionPool进行管理)
 - DataSourceProxy封装ConnectionPool/PoolConfiguration,提供对外连接的获取,关闭,连接属性获取
 - DataSource负责mbean的相关操作
## ConnectionPool---->连接池
- 实际负责连接的创建、归还、关闭、释放
- BlockingQueue<PooledConnection> busy---->存放正在使用的连接
- BlockingQueue<PooledConnection> idle---->存放闲散的连接
- AtomicInteger waitcount---->当前等待连接的线程数
- PoolCleaner poolCleaner
  - 负责清理闲散连接,例如checkAbandoned、checkIdle、testAllIdle
- 创建连接---->org.apache.tomcat.jdbc.pool.ConnectionPool.getConnection()
    - borrowConnection
      - idle.poll---->若闲散队列无,则创建
        - org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection---->创建连接
          - 委托给org.apache.tomcat.jdbc.pool.PooledConnection.connect---->委托给驱动管理器创建连接
            - org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver
      - org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection将创建的连接插入busy.offer(con)
- 创建代理连接---->org.apache.tomcat.jdbc.pool.ConnectionPool.setupConnection
  - 创建ProxyConnection对象,利用链式串联起JdbcInterceptor,最后创建连接的代理对象connection
  - connection = (Connection)proxyClassConstructor.newInstance(new Object[] { new DisposableConnectionFacade(handler) })
- 归还连接---->归还到连接池
    - 通过创建的代理对象,进行close关闭时,回调
    - org.apache.tomcat.jdbc.pool.interceptor.AbstractCreateStatementInterceptor.invoke
    - 如果是close方法,则进行return getNext().invoke(proxy,method,args),一系列的拦截器进行操作后,最后调用
    - org.apache.tomcat.jdbc.pool.ProxyConnection.invoke中进行连接的归还---->pool.returnConnection(poolc)
    - org.apache.tomcat.jdbc.pool.ConnectionPool.returnConnection
      - 将连接从busy.remove(con)移除,插入idle.offer(con)
- 释放连接---->将连接关闭
  - org.apache.tomcat.jdbc.pool.ConnectionPool.release
  - 如果存在线程等待连接waitcount.get()>0,则创建连接放入idle.offer(create(true))
## PoolConfiguration---->连接池配置对象
- DefaultProperties---->PoolProperties---->PoolConfiguration
- 常用的默认参数(initialSize----10、DEFAULT_MAX_ACTIVE----100、maxWait----30000)
- useStatementFacade----true、useDisposableConnectionFacade----true
## JdbcInterceptor---->拦截器(InvocationHandler)
- 以java代理回调方式进行方法的拦截,通过链式的拦截器操作,在连接方法调用时进行相关的拦截操作
- JdbcInterceptor next ---->组成链式的拦截器链
- getNext().invoke(proxy,method,args)---->拦截器链式操作,并进行相应的回调
## 测试
- org.apache.tomcat.jdbc.test.SimplePOJOExample---->连接mysql简单示例
- org.apache.tomcat.jdbc.test.TestSlowQueryReport---->慢sql演示