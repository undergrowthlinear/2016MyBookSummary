# HttpMessageConverter-spring(4.3.10) 学习笔记
## 概述
- 参考
    - https://my.oschina.net/lichhao/blog/172562
    - HttpMessageConverter用于解决http消息与Java对象的阻抗问题
## HttpMessageConverter
- read---->HttpMessageConverter支持将HttpInputMessage转为T(根据canRead/mediaType)
- write---->HttpMessageConverter支持将T转为HttpOutputMessage(根据canWrite/mediaType)
### 普通对象转换器---->byte/string
- ByteArrayHttpMessageConverter---->AbstractHttpMessageConverter---->HttpMessageConverter
    - AbstractHttpMessageConverter提供supportedMediaTypes存储,defaultCharset存储,
      - 提供supports方法用于子类判断对象T是否支持,提供readInternal子类覆写
      - 提供writeInternal子类覆写(在write方法写之前,会附加默认content-type/content-length头信息)
    - ByteArrayHttpMessageConverter提供byte[]与http消息的转换
      - 默认优先支持application/octet-stream以及*/*
      - supports---->byte[].class == clazz
      - 通过ByteArrayOutputStream对象进行字节流的转换
- StringHttpMessageConverter---->AbstractHttpMessageConverter---->HttpMessageConverter
    - 默认优先支持text/plain以及*/*
    - supports---->String.class == clazz
    - 通过StreamUtils工具进行字节码与字符串的转换
### 泛型对象转换器----json
- GsonHttpMessageConverter---->AbstractGenericHttpMessageConverter---->GenericHttpMessageConverter---->HttpMessageConverter
    - GenericHttpMessageConverter提供支持泛型信息(java.lang.reflect.Type)的canRead/read/canWrite/write
    - AbstractGenericHttpMessageConverter提供按照mediaType进行消息的转换机制,提供泛型信息的writeInternal
    - GsonHttpMessageConverter
      - 利用Gson按照UTF-8编解码,支持application/json,application/*+json头信息
      - read---->readTypeToken---->this.gson.fromJson(json, token.getType())
      - write附加完相应的头信息后,writeInternal利用this.gson.toJson(o, type, writer)写入转换的T对象
- MappingJackson2HttpMessageConverter---->AbstractJackson2HttpMessageConverter---->AbstractGenericHttpMessageConverter---->GenericHttpMessageConverter---->HttpMessageConverter
    - MappingJackson2HttpMessageConverter提供与gson等同的转换功能
## EnableWebMvc---->启用WebMvcConfigurationSupport支持,添加一系列mvc支持
- HandlerMapping/HandlerAdapter/HandlerExceptionResolverComposite/HttpMessageConverter等
- WebMvcConfigurationSupport.addDefaultHttpMessageConverters添加如下转换器
    - ByteArrayHttpMessageConverter/StringHttpMessageConverter/ResourceHttpMessageConverter
    - SourceHttpMessageConverter/AllEncompassingFormHttpMessageConverter
    - MappingJackson2HttpMessageConverter或者GsonHttpMessageConverter
## RequestResponseBodyMethodProcessor---->利用HttpMessageConverter解析RequestBody与ResponseBody标示的参数与返回值
- AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters利用转换器处理消息与Java对象映射
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git