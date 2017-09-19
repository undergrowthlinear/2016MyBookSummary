# HttpMessageConverters-spring-boot(1.5.6)-cloudfeign1.3.4 学习笔记
## 概述
  - http://blog.csdn.net/undergrowth/article/details/78003563
## springboot---->HttpMessageConverters
- 当创建HttpMessageConverters时,会调用getDefaultConverters获取WebMvcConfigurationSupport中配置的转换器
- 而getCombinedConverters会将用户配置的转换器放置在默认转换器之前---->combined.addAll(0, processing)
## cloud-feign中利用HttpMessageConverters进行消息的转换
- 当EnableFeignClients时候,会引入FeignClientsConfiguration配置feign相关的Decoder/Encoder
- Decoder通过new ResponseEntityDecoder(new SpringDecoder(this.messageConverters))引入配置的消息转换器
- Encoder通过new SpringEncoder(this.messageConverters)引入配置的消息转换器
- 最终委托给HttpMessageConverterExtractor.extractData利用转换器转换消息
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git