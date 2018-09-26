# elasticsearch 5.2.2 学习笔记之源码阅读3-插件机制
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://my.oschina.net/u/1792341/blog/913109
    - https://elasticsearch.cn/article/450
    - https://elasticsearch.cn/article/339
## 插件作用
```
An extension point allowing to plug in custom functionality
这里以分析器插件为例,其他的还有很多插件
```
- ActionPlugin/DiscoveryPlugin/NetworkPlugin/ScriptPlugin
- ClusterPlugin/MapperPlugin/RepositoryPlugin/SearchPlugin
### 核心接口与类
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
- Releasable(Closeable)
    - AbstractComponent
        - PluginsService
            - plugins(用于记录一系列的模块与插件)
- Plugin
    - 提供模块/服务类/组件/配置信息机制
- AnalysisPlugin
    - CharFilterFactory(字符过滤器工厂)
    - TokenFilterFactory(词源过滤器工厂)
    - TokenizerFactory(分词器工厂)
    - Analyzer(分析器)
- AnalysisRegistry(字符过滤器、词源过滤器、分词器、分析器的集合类)
    - An internal registry for tokenizer, token filter, char filter and analyzer
### 以ik自定义插件为例讲解加载过程
- https://github.com/medcl/elasticsearch-analysis-ik/tree/v5.2.2
- Node创建时创建PluginsService,以classpathPlugins/modulesDirectory/pluginsDirectory三种方式加载扩展插件
    - 在加载的时候通过PluginInfo#readFromProperties解析plugin-descriptor.properties文件,解析name/description/version/classname等属性,例如ik的就是org.elasticsearch.plugin.analysis.ik.AnalysisIkPlugin
- 将解析出来的插件与其他插件机制融合,例如AnalysisPlugin,在node创建时创建AnalysisModule同时传入pluginsService.filterPlugins(AnalysisPlugin.class)实现AnalysisPlugin的插件,例如AnalysisIkPlugin
- AnalysisModule创建时设定默认的字符过滤器/词源过滤器/分词器/分析器,同时根据AnalysisPlugin的方法获取自定义插件支持的值
    - setupCharFilters(默认html_strip/pattern_replace/mapping)
    - setupTokenFilters(默认stop等等)
    - setupTokenizers(默认standard/keyword/letter等等)
    - setupAnalyzers(默认standard/simple/chinese等等)
- 将默认的和插件提供的均封装在AnalysisRegistry,供其他模块使用
    


        
