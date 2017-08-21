# retrofit2.3.0 学习笔记
## 概述
- 参考
  - http://blog.csdn.net/ljd2038/article/details/51046512
  - http://www.jianshu.com/p/3e13e5d34531
- 属于square的okio/okhttp的延伸,基于okhttp,将java接口转为http请求与响应
- Retrofit---->入口
- Converter---->对象转换器,转换java对象到http对应的RequestBody/ResponseBody
- CallAdapter---->适配okhttp的Call与java接口响应
- OkHttpCall---->Call实现,用于转换java接口为封装Request的Call,执行Call,转换响应为Response
- ServiceMethod---->记录java接口相关信息(方法注解,参数注解,参数类型),适配java接口信息与http请求/响应信息
- ParameterHandler---->用于java接口方法的参数注解解析器,将相关参数注解信息转为http请求的相关信息
## 测试
- com.example.retrofit.SimpleService