# apache-comnons系列之commons-configuration2 学习笔记
## 概述
- 参考
  - http://blog.csdn.net/werder/article/details/54562609
  - http://blog.csdn.net/mesufy/article/details/70332799
## 加载配置流程
  - 以PropertiesConfiguration-->FileBasedConfiguration-->Configuration/FileBased
  - 以PropertiesConfiguration-->BaseConfiguration-->AbstractConfiguration-->Configuration/BaseEventSource
  - Configuration-->ImmutableConfiguration/SynchronizerSupport
### 策略、工厂、组合
---
```
利用FileHandler的load方法委托FileBased加载FileLocator指向的文件流，回调PropertiesConfiguration的read
委托给PropertiesConfigurationLayout的load利用DefaultIOFactory创建PropertiesReader,调用PropertiesReader的
nextProperty获取(readProperty)并解析(parseProperty)属性,将解析出来的属性利用PropertiesConfiguration的propertyLoaded
方法调用addPropertyInternal--addPropertyDirect添加属性到BaseConfiguration的store map对象中
```
---
## 创建配置对象流程(生成器模式)
  - ReloadingFileBasedConfigurationBuilder-->FileBasedConfigurationBuilder-->BasicConfigurationBuilder
  - ConfigurationBuilder-->EventSource
### 利用生成器模式创建相关对象
---
```
通过ReloadingFileBasedConfigurationBuilder的configure配制BuilderParameters参数,例如Parameters设置fileBased文件路径(利用代理模式)
通过getConfiguration获取配置对象,并进行配置对象的初始化(委托给FileBasedConfigurationBuilder的initFileHandler,利用其加载文件,
剩下过程如配置对象加载)
```
---
## 测试
- org.apache.commons.configuration2.TestPropertiesConfiguration
    - 找到FileLocator指向的文件利用FileLocatorUtils.locate查找(使用FileLocationStrategy策略模式)
    - 创建PropertiesReader,利用DefaultIOFactory工厂模式创建PropertiesReader
    - interpolatedConfiguration
      - 获取支持变量替换的配置对象,委托ConfigurationInterpolator的interpolate利用Lookup的lookup查找变量进行替换
- org.apache.commons.configuration2.builder.TestReloadingFileBasedConfigurationBuilder
    - getConfiguration(BasicConfigurationBuilder)
        - createResult(BasicConfigurationBuilder)
          - createResultInstance(BasicConfigurationBuilder)
            - initResultInstance(FileBasedConfigurationBuilder)
              - initFileHandler(FileBasedConfigurationBuilder)
                - handler.locate();
                - handler.load();