# fastjson1.2.37 学习笔记
## 概述
- 参考
  - https://github.com/alibaba/fastjson
  - https://github.com/Alibaba/fastjson/wiki/%E9%A6%96%E9%A1%B5
  - https://github.com/alibaba/fastjson/wiki/JSON_API_cn
- JSON---->入口
- ObjectSerializer---->序列化
  - JavaBeanSerializer---->ObjectSerializer
  - JavaBeanSerializer---->SerializeFilterable
- SerializeFilter---->序列化过滤器
- JSONSerializer/SerializeWriter---->转换支持工具
- ObjectDeserializer---->反序列化
- DefaultJSONParser---->反序列化解析处理器
- JSONReader/JSONLexer
- JSONField/JSONType/JSONPOJOBuilder---->注解支持
- FastJsonHttpMessageConverter---->spring支持
## 测试
-