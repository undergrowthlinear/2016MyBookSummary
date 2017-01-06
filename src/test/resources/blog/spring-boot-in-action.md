#2016书单总结--spring-boot-in-action学习笔记--源码简析
1. 使用spring-boot主要使用起步依赖(spring-boot-starters)、自动配置(AutoConfigure)、监控管理(Actuator)、至于命令行工具(CLI),
老实说并不是太喜欢
2. 起步依赖其实利用了Maven的传递依赖以及Actuator模块中的可选非传递依赖
3. 通过对spring-boot启动源码进行简单分析，查看spring-boot是如何启动的，自动配置是如何完成的、监控管理是如何加入进来的
    a. 通过https://start.spring.io/ 构建初始化项目，加入web、jpa、h2起步依赖
    b. github下载spring-boot源码，https://github.com/spring-projects/spring-boot 导入idea，切换到1.4.3.BUILD-SNAPSHOT分支
    c. 本项目源码位于 https://github.com/undergrowthlinear/2016MyBookSummary.git 
4. 从@SpringBootApplication 开始分析
@SpringBootApplication
public class MybooksummaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybooksummaryApplication.class, args);
	}
}
@SpringBootApplication中包含三个重要注解
@SpringBootConfiguration其实就是个@Configuration
@ComponentScan组件扫描
@EnableAutoConfiguration有比较重要的@Import(EnableAutoConfigurationImportSelector.class)
    EnableAutoConfigurationImportSelector负责加载需要自动配置的类文件，
    org.springframework.boot.autoconfigure.EnableAutoConfigurationImportSelector.selectImports的
    List<String> configurations = getCandidateConfigurations(metadata,attributes);负责利用SpringFactoriesLoader.loadFactoryNames从
    "META-INF/spring.factories"的文件中加载需要的自动配置类
    在spring-boot-autoconfigure\src\main\resources\META-INF\spring.factories可以找到关于自动配置的很多项
      spring-boot-actuator\src\main\resources\META-INF\spring.factories关于监控的自动配置的很多项
资源找到了，那么是谁来解析他们的呢，接着看
5. 从启动源码SpringApplication.run-->
org.springframework.boot.SpringApplication.initialize---->确定this.webEnvironment是否为web环境，取决于类路径下是否有
   { "javax.servlet.Servlet","org.springframework.web.context.ConfigurableWebApplicationContext" }
再看org.springframework.boot.SpringApplication.run(java.lang.String...)中做了很多操作，关注重点几个
a---->context = createApplicationContext();---->当web环境时，创建"org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext"的上下文，用于解析注解的web上下文环境

b---->prepareContext(context, environment, listeners, applicationArguments,printedBanner);---->重要的方法就是
     org.springframework.boot.SpringApplication.load---->加载资源
            BeanDefinitionLoader loader = createBeanDefinitionLoader(getBeanDefinitionRegistry(context), sources);---->创建加载器
                    loader.load();
                        org.springframework.boot.BeanDefinitionLoader.load(java.lang.Object)---->按照字节码加载
                            org.springframework.boot.BeanDefinitionLoader.load(java.lang.Class<?>)
                                if (isComponent(source)) { ---->判断是否具有Component注解,因为Configuration注解其实是用@Component注解标示的
                                			this.annotatedReader.register(source);---->将字节码中带有的注解信息存入到BeanFactory，便于后续解析
                                			    org.springframework.context.annotation.AnnotatedBeanDefinitionReader.register
                                			     org.springframework.context.annotation.AnnotatedBeanDefinitionReader.registerBean(java.lang.Class<?>, java.lang.String, java.lang.Class<? extends java.lang.annotation.Annotation>...)
                                			       AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(annotatedClass)
                                			         org.springframework.core.type.StandardAnnotationMetadata.StandardAnnotationMetadata(java.lang.Class<?>, boolean)
上面做的一些事情 只是将需要解析的注解信息资源准备好了，但是解析注解的事情是在哪里做的呢？                              			      

c---->c.refreshContext(context); ----->有一步很关键，进行注解的解析，不啰嗦，直接跟踪代码
    多点几下，来到这
    org.springframework.context.support.AbstractApplicationContext.refresh
        org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors
           org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.List<org.springframework.beans.factory.config.BeanFactoryPostProcessor>)
            org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry
                org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions
                    org.springframework.context.annotation.ConfigurationClassParser.parse(java.util.Set<org.springframework.beans.factory.config.BeanDefinitionHolder>)
                        org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass---->在这里你就可以看到对@PropertySource、@ComponentScan、
                        @Import、@ImportResource、@Bean相关注解的解析
                        这里我们只关注@Import注解，我们来看一下，如何完成自动配置
                        
d---->org.springframework.context.annotation.ConfigurationClassParser.processImports--关注@Import
   因为我们的MybooksummaryApplication具有@SpringBootApplication注解，拥有@Import(EnableAutoConfigurationImportSelector.class)
   所以执行
   if (candidate.isAssignable(ImportSelector.class)) 逻辑
     通过selector.selectImports回到org.springframework.boot.autoconfigure.EnableAutoConfigurationImportSelector.selectImports
     获取到需要处理的自动配置列表后，递归
        processImports(configClass, currentSourceClass, importSourceClasses, false);方法
        来到org.springframework.context.annotation.ConfigurationClassParser.processConfigurationClass逻辑
        进行@PropertySource、@ComponentScan、 @Import、@ImportResource、@Bean的处理
 针对上面的原理，讲个实例
 
e---->例如内嵌的tomcat,启动在8080端口,是怎么搞的呢？
 在spring-boot-autoconfigure\src\main\resources\META-INF\spring.factories找到
 org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration
会看到主要几个注解
@ConditionalOnWebApplication----only matches when the application context is a web application
@ConditionalOnClass(DispatcherServlet.class)----only matches when the specified classes are on the classpath
@AutoConfigureAfter(EmbeddedServletContainerAutoConfiguration.class)----Hint for that an {@link EnableAutoConfiguration auto-configuration} should be applied after other specified auto-configuration classes.
在DispatcherServletAutoConfiguration里面有如下代码，看完上面的内容，你就知道了，这里会帮你添加一个DispatcherServlet，等同于在web.xml中进行配置
                           @Bean(name = DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
                           		public DispatcherServlet dispatcherServlet() {
                           			DispatcherServlet dispatcherServlet = new DispatcherServlet();
 还有其他的视图什么的，不说了
再来看@AutoConfigureAfter(EmbeddedServletContainerAutoConfiguration.class)----这个就是用来启动tomcat的重要东西了
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration---->
里面也是有这么一段
@Configuration
	@ConditionalOnClass({ Servlet.class, Tomcat.class })
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedTomcat {

		@Bean
		public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
			return new TomcatEmbeddedServletContainerFactory();
		}

	}
这个org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory就是创建内嵌的tomcat容器工厂了
  org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory.getEmbeddedServletContainer
     创建Tomcat tomcat = new Tomcat();
           关联连接器tomcat.setConnector(connector);
           配置引擎configureEngine(tomcat.getEngine());
           这是上下文信息org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory.prepareContext
           创建tomcat容器----org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory.getTomcatEmbeddedServletContainer
           org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer#TomcatEmbeddedServletContainer(Tomcat, boolean)
            this.tomcat.start();
            
以上即是spring-boot的起步依赖、自动配置、监控相关源码解析，如有不对之处，请多多指教！！----201701070057