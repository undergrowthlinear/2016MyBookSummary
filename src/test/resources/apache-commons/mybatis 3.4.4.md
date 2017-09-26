# mybatis 3.4.4 学习笔记
## 概述
- 参考
  - http://www.imooc.com/article/1291
  - http://www.jianshu.com/p/a341bae7fcfc
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
  - MapperRegistry mapperRegistry
  - Map String, MappedStatement mappedStatements
### 解析mapper为MappedStatement---->解析定义的mapper文件
- XMLMapperBuilder#parse
## 接口请求/执行/结果返回
### 创建SqlSession
### 接口参数解析
### 接口执行
### 接口结果解析
## 测试
-