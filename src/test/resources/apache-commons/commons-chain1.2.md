# apache-comnons系列之commons-chain1.2 学习笔记
## 概述
- 参考
  - http://blog.csdn.net/pengchua/article/details/1955640
  - http://blog.csdn.net/wangyongxia921/article/details/50635278
  - http://blog.csdn.net/ffm83/article/details/42263419
## Command
````
A {@link Command} encapsulates a unit of processing work to be performed, whose purpose is to examine and/or modify the state of a
 transaction that is represented by a {@link Context}.  Individual {@link Command}s can be assembled into a {@link Chain}, which allows
 them to either complete the required processing or delegate further processing to the next {@link Command} in the {@link Chain}.
````
  - execute
### Chain---->Command
````
A {@link Chain} represents a configured list of{@link Command}s that will be executed in order to perform processing
 on a specified {@link Context}.  Each included {@link Command} will be executed in turn, until either one of them returns <code>true</code>,
 one of the executed {@link Command}s throws an exception,or the end of the chain has been reached.  The {@link Chain} itself will
 return the return value of the last {@link Command} that was executed (if no exception was thrown), or rethrow the thrown exception
````
  - addCommand
#### ChainBase---->Chain---->Command
````
Convenience base class for {@link Chain} implementations
````
  - commands = new Command[0];
  - frozen = false;(Flag indicating whether the configuration of our commands list has been frozen by a call to the <code>execute()</code> method)
  - execute---->通过正向遍历commands数据执行命令的execute方法,通过逆向遍历执行过滤器的postprocess方法
### Filter---->Command
````
A {@link Filter} is a specialized {@link Command} that also expects the {@link Chain} that is executing it to call the
 <code>postprocess()</code> method if it called the <code>execute()</code> method.  This promise must be fulfilled in spite of any possible
 exceptions thrown by the <code>execute()</code> method of this {@link Command}, or any subsequent {@link Command} whose
 <code>execute()</code> method was called.  The owning {@link Chain} must call the <code>postprocess()</code> method of each {@link Filter}
 in a {@link Chain} in reverse order of the invocation of their <code>execute()</code> methods.
````
  - postprocess
#### LookupCommand---->Filter---->Command
````
Look up a specified {@link Command} (which could also be a {@link org.apache.commons.chain.Chain})
 in a {@link Catalog}, and delegate execution to it.  If the delegated-to {@link Command} is also a {@link Filter}, its <code>postprocess()</code>
 method will also be invoked at the appropriate time.
````
  - execute
    - 先在catalog中查找命令,然后调用命令执行，类似的还有DispatchCommand,不过分发命令利用了反射去查找/执行方法
## Context---->Map
````
A {@link Context} represents the state information that is accessed and manipulated by the execution of a {@link Command} or a
 {@link Chain}.  Specialized implementations of {@link Context} will typically add JavaBeans properties that contain typesafe accessors
 to information that is relevant to a particular use case for this context, and/or add operations that affect the state information
 that is saved in the context.
````
### ContextBase---->Context
````
Convenience base class for {@link Context} implementations.
````
#### ServletWebContext---->WebContext---->ContextBase---->Context
````
Concrete implementation of {@link WebContext} suitable for use in Servlets and JSP pages.  The abstract methods are mapped to the appropriate
 collections of the underlying servlet context, request, and response instances that are passed to the constructor (or the initialize method).
````
## Catalog
````
A {@link Catalog} is a collection of named {@link Command}s (or{@link Chain}s) that can be used to retrieve the set of commands that
 should be performed based on a symbolic identifier.  Use of catalogs is optional, but convenient when there are multiple possible chains
 that can be selected and executed based on environmental conditions.
````
  - CatalogBase---->Catalog
    - commands = Collections.synchronizedMap(new HashMap());
## CatalogFactory
````
A {@link CatalogFactory} is a class used to store and retrieve {@link Catalog}s.  The factory allows for a default {@link Catalog}
 as well as {@link Catalog}s stored with a name key.  Follows the Factory pattern (see GoF).
````
  - factories = new HashMap();
  - CatalogFactoryBase---->CatalogFactory
## ConfigParser
````
Class to parse the contents of an XML configuration file (using Commons Digester) that defines and configures commands and command chains
 to be registered in a {@link Catalog}.  Advanced users can configure the detailed parsing behavior by configuring the properties of an instance
 of this class prior to calling the <code>parse()</code> method.  It is legal to call the <code>parse()</code> method more than once, in order
 to parse more than one configuration document.
````
  - parse
    - 利用digester定义规则进行解析出
## 测试
- org.apache.commons.chain.config.ConfigParser2TestCase
- org.apache.commons.chain.generic.DispatchLookupCommandTestCase
- org.apache.commons.chain.impl.ContextBaseTestCase
- org.apache.commons.chain.web.servlet.ServletWebContextTestCase