# elasticsearch 5.2.2 学习笔记之源码阅读11-脚本模块(ScriptModule)总览之脚本字段与自定义评分
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/modules-scripting.html
    - https://www.easyice.cn/archives/288
    - https://blog.csdn.net/u013613428/article/details/78134170
## 脚本模块(ScriptModule)总览之脚本字段与自定义评分
- 脚本(Script三要素lang/type/params)
    - lang(painless/groovy/javascript/python/expression等)
        - painless(内置脚本,专为elasticsearch开发)
            - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/modules-scripting-painless-syntax.html
            - java的数据类型/操作符/控制流语句均支持
        - expression(lucene表达式语言,将javascript编译为字节码)
            - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/modules-scripting-expression.html
            - 使用如 doc['field_name'].max()/doc['field_name'].date.year 等
            - Only numeric, boolean, date, and geo_point fields may be accessed
    - type
        - INLINE(内嵌的脚本/会缓存/脚本中尽量使用变量)
        - STORED(存储在ClusterState中的脚本,通过脚本id进行访问)
        - FILE(存放于config/scripts目录中,通过文件名称进行访问)
    - params(传递的变量map列表)
- 字段的不同访问方式
    - doc-values
        - 最高效/使用doc['field_name']进行访问/默认不包括可分析的text字段
    - stored fields 
        - 只使用"store": true的字段/ 使用_fields['field_name'].values
    - _source field
        - 使用_source.field_name方式访问/可访问所有字段/性能开销较大
- 进行如下操作前 先参看 https://www.elastic.co/guide/en/elasticsearch/reference/5.2/modules-scripting-painless.html 准备索引数据
- 脚本字段 postman post http://localhost:9200/hockey/_search
```
{
    "query": {
        "match_all": {}
    },
    "script_fields": {
        "total_goals": {
            "script": {
                "lang": "painless",
                "inline": "int total = 0; for (int i = 0; i < doc['goals'].length; ++i) { total += doc['goals'][i]; } return total;"
            }
        },
        "test1": {
            "script": "params['_source']['last']"
        },
        "max_goals": {
            "script": {
                "lang": "expression",
                "inline": "doc['goals'].max()"
            }
        }
    }
}
```
- 自定义评分 postman post http://localhost:9200/hockey/_search
```
{
  "query": {
    "function_score": {
      "script_score": {
        "script": {
          "lang": "painless",
          "inline": "int total = 0; for (int i = 0; i < doc['goals'].length; ++i) { total += doc['goals'][i]; } return total;"
        }
      }
    }
  }
}
```
- 排序 postman post http://localhost:9200/hockey/_search
```
{
  "query": {
    "match_all": {}
  },
  "sort": {
    "_script": {
      "type": "string",
      "order": "asc",
      "script": {
        "lang": "painless",
        "inline": "doc['first.keyword'].value + ' ' + doc['last.keyword'].value"
      }
    }
  }
}
```
### 核心接口与类
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - AbstractLifecycleComponent
        - ScriptService(脚本服务的支持类)
    - LifecycleComponent
- ScriptPlugin(脚本插件,用于加载脚本执行服务/本地脚本工厂/脚本上下文)
    - PainlessPlugin(painless语言支持的插件)
- ScriptModule(用于管理ScriptSettings和ScriptService)
- ScriptEngineService(脚本引擎的接口,定义编译脚本/执行编译脚本/将编译脚本用于搜索等)
    - PainlessScriptEngineService(painless语言支持)
    - PythonScriptEngineService(python语言支持)
    - GroovyScriptEngineService(groovy语言支持)
- ScriptSettings(脚本相关的设置信息)
- CompiledScript
- SearchScript
### 以 脚本字段 为例讲解加载过程 如之前的 postman post http://localhost:9200/hockey/_search
- node启动查看  https://blog.csdn.net/undergrowth/article/details/82840411
     - 通过PluginsService(https://blog.csdn.net/undergrowth/article/details/82857089)加载modules目录下的lang-painless插件,即加载PainlessPlugin用于构建PainlessScriptEngineService
     - 再看搜索的流程参看https://blog.csdn.net/undergrowth/article/details/83001430 是如何解析script_fields,里面的脚本是如何执行返回到原有文档中的?
     - 在前面搜索里面提到过,在SearchSourceBuilder#parseXContent的时候,解析出List<ScriptField> scriptFields的脚本语言(里面调用Script#parse进行解析)
        - 查询分两步,在第一步query阶段时候,SearchService#executeQueryPhase执行的时候创建SearchContext,在SearchService#parseSource将SearchSourceBuilder转为DefaultSearchContext,同时利用scriptService.search将ScriptField转化为SearchScript供后续使用
            - 这里分为compile(利用ScriptEngineService的实现类转换Script为CompiledScript)和search(将CompiledScript转为SearchScript),根据node启动时候的插件机制和传递的lang,这里指的是实现类是PainlessScriptEngineService
        - 第二步,在fetch阶段的时候,FetchPhase#execute执行完成后,会遍历FetchSubPhase集合,进行获取子阶段处理,这里看ScriptFieldsFetchSubPhase#hitExecute遍历query阶段解析出来的ScriptFieldsContext.ScriptField,进行脚本的运行leafScript.unwrap(leafScript.run()),这里调用painless.ScriptImpl#run执行脚本,然后修改hitContext.hit().fields().put(scriptField.name(), hitField),完成脚本字段的解析
### 以 自定义评分 为例讲解加载过程(待更新)