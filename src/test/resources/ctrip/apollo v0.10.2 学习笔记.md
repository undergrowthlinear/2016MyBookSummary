# apollo v0.10.2 学习笔记
## 概述
- 参考
    - https://github.com/ctripcorp/apollo ----官方code
    - https://github.com/ctripcorp/apollo/wiki/Apollo%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83%E8%AE%BE%E8%AE%A1   ----Apollo配置中心设计
    - https://github.com/ctripcorp/apollo/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%83%A8%E7%BD%B2%E6%8C%87%E5%8D%97  ----分布式部署指南
    - https://github.com/ctripcorp/apollo-use-cases     ----使用场景和示例代码
    - https://raw.githubusercontent.com/ctripcorp/apollo/master/doc/images/lyliyongblue-apollo-deployment.png   ----生产部署参考图
    - apollo文档写的真心好,很少见到这么完善的文档了,点赞
## 分布式部署的时候,可能踩的坑
- 可能出现hibernate.dialect not set
    - 解决:在apollo-common的application.properties里面加入spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
- 使用打包好的startup.sh启动admin/config服务的时候,出现spring.datasource相关信息错误(在编译已经修改了scripts/build.bat中的url/username/password)
    - 解决:在相应的startup.sh中通过JAVA_OPTS注入spring.datasource.url等信息
    - eg:
    ```
    export JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.url=jdbc:mysql://xxxx.lc:3306/apolloconfigdb_fat?characterEncoding=utf8
    -Dspring.datasource.username=xxxx
    -Dspring.datasource.password=xxxx -Dserver.port=$SERVER_PORT -Dlogging.file=$LOG_DIR/$SERVICE_NAME.log -Xloggc:$LOG_DIR/heap_trace.txt -XX:HeapDumpPath=$LOG_DIR/HeapDumpOnOutOfMemoryError/
    ```
- mysql级别的参数需要----lower_case_table_names=1(mysql数据库表名不区分大小写)
## 模块
- Config服务、Admin服务、Meta服务
    - Meta服务用于封装Config/Admin,对外提供统一的访问入口
    - Config提供Client获取配置信息
    - Admin用于接收Portal修改的配置信息推送
    - Admin与Config的配置信息改变,通过对数据库表releasemessage的监控进行通信
- Portal服务
    - 提供web界面操作,发布信息给相应Env的Admin服务
- Client
    - 通过Meta获取Config服务后,直连Config,采取LongPoll方式获取配置改变
## 源码分析
### Portal入口----发布配置
- portal.controller.ReleaseController#createRelease
    - ReleaseService#publish----省略部分不重要步骤
        - RetryableRestTemplate#execute----支持重试的rest
            - getAdminServices----获取对应Env的admin服务地址,实质上是来自于metaservice.controller.ServiceController#getAdminService的调用
            - doExecute----进行admin服务调用,发布改变配置信息,调用adminservice.controller.ReleaseController#publish
### Admin服务----发布(当接收到Portal消息后,如下)
-  adminservice.controller.ReleaseController#publish
    - DatabaseMessageSender#sendMessage
        - releaseMessageRepository.save----保存到ReleaseMessage表里面
### Config服务
- 在configservice.ConfigServiceAutoConfiguration注入ReleaseMessageScanner
    - ReleaseMessageScanner的afterPropertiesSet进行scanMessages
        - ReleaseMessageScanner#scanAndSendMessages
        - releaseMessageRepository.findFirst500ByIdGreaterThanOrderByIdAsc
        - fireMessageScanned
            - ConfigFileController#handleMessage----记录改变的配置信息key
            - NotificationControllerV2#handleMessage----记录改变配置信息
### Client端
- 通过EnableApolloConfig引入相应的注解解析器，eg:ApolloAnnotationProcessor/ApolloJsonValueProcessor
- ApolloAnnotationProcessor----解析ApolloConfig/ApolloConfigChangeListener注解
    - ApolloAnnotationProcessor#processField
        - ConfigService#getConfig----通过http请求获取对应命名空间的配置信息
            - DefaultConfigManager#getConfig
                - DefaultConfigFactory#create
                - DefaultConfigFactory#createLocalConfigRepository
                - RemoteConfigRepository#RemoteConfigRepository
                    - RemoteConfigRepository#sync----获取配置服务信息,同步远程配置信息
                        - getConfigServices----获取配置服务地址,实际上调用ServiceController#getConfigService
                        - m_httpUtil.doGet----com.ctrip.framework.apollo.configservice.controller.ConfigController#queryConfig
                    - 除了sync同步外,RemoteConfigLongPollService进行LongPoll的方式获取配置信息

        
