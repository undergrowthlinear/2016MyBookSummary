# spring-cloud-zuul之spring-cloud-1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
## 概述
- 参考
    - http://blog.didispace.com/springcloud5/
    - http://blog.didispace.com/spring-cloud-source-zuul/
    - http://blog.csdn.net/undergrowth/article/details/78035935
## EnableZuulProxy---->开启ZuulProxyMarkerConfiguration.Marker开关,打开ZuulProxyAutoConfiguration配置
- 引入EnableCircuitBreaker熔断支持
- 引入EnableDiscoveryClient服务发现支持
## ZuulProxyAutoConfiguration---->zuul代理自动配置,继承ZuulServerAutoConfiguration
- 注入DiscoveryClient,支持eureka服务发现
- 注入PreDecorationFilter,pre过滤器,用于在RequestContext设置是routeHost还是serviceId,给route级别过滤器进行处理
- 注入RibbonRoutingFilter,route过滤器,支持ribbon/hystrix构建RibbonCommand(HystrixCommand)以serviceId配置方式进行路由
- 注入SimpleHostRoutingFilter,route过滤器,支持直接url配置方式以httpclient方式进行路由
## ZuulServerAutoConfiguration---->配置spring-zuul支持,将配置path进行路由转换
- 注入ZuulProperties,用于解析配置的zuul.routes的配置信息
- 注入ZuulController控制器,引入ZuulServlet,连接spring-zuul与zuul的桥梁
- 注入ZuulHandlerMapping映射器,将配置在ZuulProperties中的path映射到ZuulController中的ZuulServlet进行一系列的过滤器处理
- 注入pre/post级别的过滤器(filters)
- 注入ZuulFilterInitializer初始化,用于将注入的filters引入到zuul体系,通过filterRegistry引入
- 注入一系列的指标监控信息相关bean
## 其他支持
- FilterConstants---->过滤器级别常量等支持
- SimpleRouteLocator---->RouteLocator---->路由定位支持,通过path找到Route信息
- ZuulProperties---->zuul属性支持,存放routes以Map的路由信息
- HttpClientRibbonCommandFactory---->AbstractRibbonCommandFactory---->RibbonCommandFactory---->httpclient的ribboncommand支持
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git