# mybatis 3.4.4 学习笔记
## 概述
- 参考
  - http://www.imooc.com/article/1291
  - http://www.jianshu.com/p/a341bae7fcfc
  - http://www.cnblogs.com/sin90lzc/archive/2012/06/30/2571175.html
  - http://wiki.jikexueyuan.com/project/mybatis-in-action/interface-programming.html
## SqlSessionFactoryBuilder---->mybatis入口
- 利用配置文件解析SqlSessionFactory对象
## 解析配置文件---->将配置文件(eg:mybatis.xml)与Mapper文件(eg:UserMapping.xml)解析成相应的Java对象
### 解析configuration为Configuration---->获取通用属性/配置/插件/类型处理等
- XMLConfigBuilder#parseConfiguration---->解析配置在配置文件中的元素
    - settings/typeAliases/plugins/objectFactory/objectWrapperFactory/reflectorFactory/databaseIdProvider/typeHandlers
    - properties---->解析properties元素下配置的name/value构成一个property
      - 同时支持resource/url配置的属性,将所有解析属性置于Configuration的variables,供后续使用
    - environments---->获取指定environment的dataSource/transactionManager
    - mapperElement(root.evalNode("mappers"))---->以XMLMapperBuilder解析mapper元素为MappedStatement
- 重要属性
    - MapperRegistry mapperRegistry---->存放以mapper类型为key,其MapperProxyFactory为value的Map
    - Map String, MappedStatement mappedStatements---->以namespace.id为key,MappedStatement存放的Map,可直接通过合成id调用执行
### 解析mapper为MappedStatement---->解析定义的mapper文件
- XMLMapperBuilder#parse
    - 解析namespace参数
    - 解析cache-ref/cache/resultMap/sql/select|insert|update|delete元素配置
    - 解析resultMap
      - 解析id/type属性,以及旗下result元素的column/property等元素,合成ResultMapping对象,最终合成ResultMap对象
    - 解析sql
      - 将sql片段以namespace.id形式为key,XNode为value存放于sqlFragments
    - 解析select|insert|update|delete
      - 将配置的id/parameterType/resultMap/resultType/statementType(默认为PREPARED)/sqlCommandType/sqlSource,合成MappedStatement对象
      - 解析sqlSource对象,利用XMLLanguageDriver#createSqlSource,委托XMLScriptBuilder#parseScriptNode,
      - 根据sql内容中是否有${}创建DynamicSqlSource还是RawSqlSource
## 接口请求/执行/结果返回---->利用DefaultSqlSessionFactory对象创建SqlSession,调用namespace.id合成的语句,进行参数解析,请求语句执行,结果返回解析
### 创建SqlSession
- DefaultSqlSessionFactory#openSession
    - 利用之前解析出来的configuration/executor(默认创建SimpleExecutor执行器)/autoCommit(默认为false)创建DefaultSqlSession
### 接口参数解析/接口执行/接口结果解析
- DefaultSqlSession#selectOne(java.lang.String, java.lang.Object)---->namespace.id方式进行调用
    - DefaultSqlSession#selectList(java.lang.String, java.lang.Object, org.apache.ibatis.session.RowBounds)
      - 从configuration的mappedStatements中以namespace.id找到之前解析出来的MappedStatement
        - 调用BaseExecutor#query进行boundSql解析,利用之前解析出来的sqlSource对象
        - 进行createCacheKey的处理
        - 当初始执行,无resultHandler,则进行queryFromDatabase
        - 回调子类的SimpleExecutor#doQuery进行真正的参数解析/语句执行/结果解析
          - 默认创建PreparedStatementHandler语句处理器
          - PreparedStatementHandler#parameterize进行参数化等处理,委托给DefaultParameterHandler#setParameters进行参数化处理
          - PreparedStatementHandler#query---->语句执行
          - 委托DefaultResultSetHandler#handleResultSets进行结果的映射处理
- DefaultSqlSession#getMapper---->以Mapper接口方式进行调用
    - Configuration#getMapper
      - MapperRegistry#getMapper
        - MapperProxyFactory#newInstance(org.apache.ibatis.session.SqlSession)---->通过工厂类,创建mapperInterface的代理对象
    - 当进行mapperInterface的方法调用时,调用代理处理器MapperProxy#invoke进行方法代理转换
      - MapperMethod#execute最终经过处理还是会转接调用DefaultSqlSession#selectList之类的方法
## 测试