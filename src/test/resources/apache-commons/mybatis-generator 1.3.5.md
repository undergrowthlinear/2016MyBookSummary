# mybatis-generator 1.3.5 学习笔记
## 概述
- 参考
  - http://www.jianshu.com/p/1b826d43dbaf
  - https://github.com/mybatis/generator
  - http://www.cnblogs.com/cb0327/p/5158178.html
  - https://github.com/abel533/Mybatis-Spring/
  - http://blog.csdn.net/hj7jay/article/details/50911968
## mybatis-generator-config_1_0.dtd---->生成器配置文件(理解此文件对于理解源码很有帮助)
- (properties?, classPathEntry*, context+)---->顶层需要至少一个content/0个或者多个classPathEntry/0个或者一个properties
### context---->上下文元素
- (property*, plugin*, commentGenerator?, (connectionFactory | jdbcConnection), javaTypeResolver?,
                           javaModelGenerator, sqlMapGenerator?, javaClientGenerator?, table+)
- 至少一个connectionFactory或者jdbcConnection,一个javaModelGenerator,一个或者多个table
- 需要0个或者多个property/plugin
- 0个或者一个commentGenerator/sqlMapGenerator/javaClientGenerator/javaTypeResolver
## MyBatisGenerator---->生成器入口
- org.mybatis.generator.api.MyBatisGenerator#generate
    - context.generateFiles---->产生generatedJavaFiles/generatedXmlFiles列表
      - 对于所有配置的introspectedTables,遍历introspectedTables,
        - 先利用IntrospectedTableMyBatis3Impl#calculateGenerators添加一系列的默认client/model/xml的生成器,产生一系列的model/example/xml/java文件
        - 再利用pluginAggregator进行如下操作
          - PluginAggregator#contextGenerateAdditionalJavaFiles(org.mybatis.generator.api.IntrospectedTable)---->PluginAggregator利用配置的plugin集合产生java文件
          - PluginAggregator#contextGenerateAdditionalXmlFiles(org.mybatis.generator.api.IntrospectedTable)---->PluginAggregator利用配置的plugin集合产生xml文件
    - writeGeneratedXmlFile---->写xml到磁盘
    - writeGeneratedJavaFile---->写java文件到磁盘
### AbstractGenerator---->产生器父类
- ExampleGenerator---->AbstractJavaGenerator---->AbstractGenerator
- SimpleJavaClientGenerator---->AbstractJavaClientGenerator---->AbstractJavaGenerator---->AbstractGenerator
- SimpleXMLMapperGenerator---->AbstractXmlGenerator---->AbstractGenerator
    - AbstractGenerator---->提供拥有context/introspectedTable/warnings/progressCallback机制
    - AbstractJavaGenerator---->提供拥有getCompilationUnits/getGetter/addDefaultConstructor/getRootClass机制
    - SimpleJavaClientGenerator---->添加getCompilationUnits实现，产生GeneratedJavaFile文件
    - SimpleXMLMapperGenerator---->添加默认方法映射,产生XmlElement文件
### ConfigurationParser---->Configuration---->所有配置项对应类
- org.mybatis.generator.config.xml.ConfigurationParser#parseConfiguration(org.xml.sax.InputSource)
### PropertyHolder---->属性拥有者,下面关联很多配置子类
- TableConfiguration/JavaModelGeneratorConfiguration/Context---->PropertyHolder
- SqlMapGeneratorConfiguration/JDBCConnectionConfiguration---->PropertyHolder
- TypedPropertyHolder---->PropertyHolder
    - JavaClientGeneratorConfiguration/PluginConfiguration/JavaTypeResolverConfiguration---->TypedPropertyHolder---->PropertyHolder
    - CommentGeneratorConfiguration/ConnectionFactoryConfiguration---->TypedPropertyHolder---->PropertyHolder
## PluginAdapter---->Plugin---->扩展的插件机制
- 遍历introspectedTables,PluginAggregator利用配置的plugin集合产生java文件/xml文件
- 理解其内涵,了解mybatis-generator-config_1_0.dtd/SimpleJavaClientGenerator/SimpleXMLMapperGenerator机制即可很好的进行扩展
## 测试