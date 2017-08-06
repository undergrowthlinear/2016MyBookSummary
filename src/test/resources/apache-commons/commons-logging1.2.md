# apache-comnons系列之commons-logging1.2 学习笔记
## 概述
- 参考
  - http://blog.csdn.net/linshuhe1/article/details/53761571
## Log
- A simple logging interface abstracting logging APIs.  In order to be instantiated successfully by {@link LogFactory}, classes that implement
- this interface must have a constructor that takes a single String parameter representing the "name" of this Log
- trace/debug/info/warn/error/fatal
## LogFactory
- Factory for creating {@link Log} instances, with discovery and configuration features similar to that employed by standard Java APIs such as JAXP.
- org.apache.commons.logging.LogFactory.getFactory
    - The <code>org.apache.commons.logging.LogFactory</code> system property.
    - The JDK 1.3 Service Discovery mechanism
    - Use the properties file <code>commons-logging.properties
    - Fall back to a default implementation class (<code>org.apache.commons.logging.impl.LogFactoryImpl</code>)
### LogFactoryImpl
  - instances = new Hashtable();---->拥有存放创建的Log
  - attributes = new Hashtable();---->用于存放加载的属性
  - classesToDiscover---->存放相关Log实现类数组(Log4JLogger/Jdk14Logger/Jdk13LumberjackLogger/SimpleLog)
  - org.apache.commons.logging.impl.LogFactoryImpl.getInstance(java.lang.String)
    - org.apache.commons.logging.impl.LogFactoryImpl#discoverLogImplementation---->查找Log的实现
      - Attempts to create a Log instance for the given category name.Follows the discovery process described in the class javadoc.
      - org.apache.commons.logging.impl.LogFactoryImpl#findUserSpecifiedLogClassName(查找是否有设定org.apache.commons.logging.Log属性)
        - Checks system properties and the attribute map for a Log implementation specified by the user under the property names {@link #LOG_PROPERTY} or {@link #LOG_PROPERTY_OLD}
      - createLogFromClass---->通过类路径查找相关实现类
        - 从classesToDiscover数组中查找相关实现类,第一个为Log4JLogger,相关加载过程如下
### Log4JLogger
  - Implementation of {@link Log} that maps directly to a <strong>Logger</strong> for log4J version 1.2.
  - org.apache.log4j.Logger logger---->Log to this logger
  - String name---->Logger name
  - org.apache.commons.logging.impl.Log4JLogger.getLogger
    - LogManager.getLogger
    - org.apache.log4j.LogManager
      - Use the <code>LogManager</code> class to retreive {@link Logger} instances or to operate on the current {@link LoggerRepository}
      - static{}---->静态初始化块,进行RootLogger、以及log4j配置文件的加载
      - OptionConverter.selectAndConfigure---->Configure log4j given a URL
        - org.apache.log4j.PropertyConfigurator.doConfigure(java.net.URL, org.apache.log4j.spi.LoggerRepository)
          - configureRootCategory(properties, hierarchy);
          - configureLoggerFactory(properties);
          - parseCatsAndRenderers(properties, hierarchy);
### Jdk14Logger
- Implementation of the <code>org.apache.commons.logging.Log</code> interface that wraps the standard JDK logging mechanisms that were introduced in the Merlin release
## 测试
- org.apache.commons.logging.LogTestCase
    - LogFactory.getLog(this.getClass().getName())---->加载过程
      - getFactory().getInstance(name)---->加载LogFactory与配置文件comomns.properties/log4j.properties文件如上所述
        - 看下如何创建我们传入的Logger对象
        - instance = (Log) logConstructor.newInstance(params);
          - 反转调用public Log4JLogger(String name)
          - org.apache.commons.logging.impl.Log4JLogger.getLogger
            - org.apache.log4j.Hierarchy.getLogger(java.lang.String, org.apache.log4j.spi.LoggerFactory)
              - logger = factory.makeNewLoggerInstance(name);---->利用工厂创建Logger
              - logger.setHierarchy(this);---->创建Logger层次
              - ht.put(key, logger);---->存放缓存的Logger
              - updateParents(logger);---->更新创建的Logger的父类