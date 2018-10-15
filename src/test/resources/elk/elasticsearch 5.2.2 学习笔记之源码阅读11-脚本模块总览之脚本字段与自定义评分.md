# elasticsearch 5.2.2 学习笔记之源码阅读11-脚本模块(ScriptModule)总览之脚本字段与自定义评分
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/modules-scripting.html
    - https://www.easyice.cn/archives/288
    - https://blog.csdn.net/u013613428/article/details/78134170
## 脚本模块(ScriptModule)总览之脚本字段与自定义评分
- 脚本字段
- 自定义评分
- 排序
- 字段的不同访问方式
    - doc-values
        - 最高效,使用doc['field_name']进行访问,默认不包括可分析的text字段
    - stored fields 
    - _source field
### 核心接口与类
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - AbstractLifecycleComponent
        - PipelineStore(将创建的管道添加到集群状态中,更新集群状态)
    - LifecycleComponent
### 以 创建管道/模拟管道使用 为例讲解加载过程