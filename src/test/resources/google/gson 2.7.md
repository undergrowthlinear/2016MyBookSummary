# gson 2.7 学习笔记
## 概述
- 参考
  - http://www.jianshu.com/p/e740196225a4
## GsonBuilder---->入口
- com.google.gson.GsonBuilder.create---->利用serializeNulls、类型适配器、FieldNamingPolicy等策略创建gson
## Gson---->主要操作类
- typeTokenCache---->用于存放以类型信息为key的类型适配器缓存
- factories---->创建json时添加一系列类型适配器工厂,例如STRING_FACTORY/ReflectiveTypeAdapterFactory
- com.google.gson.Gson.toJson(java.lang.Object, java.lang.reflect.Type, com.google.gson.stream.JsonWriter)
    - ((TypeAdapter) adapter).write(writer, src)
        - com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField.write---->将pojo的值利用反射写入JsonWriter
        - 利用TypeAdapter将T写入到JsonWriter
    - com.google.gson.Gson.fromJson(com.google.gson.stream.JsonReader, java.lang.reflect.Type)
        - object = typeAdapter.read(reader)
            - com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField.read---->pojo对象利用反射注入值
            - com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter.read
        - 利用typeAdapter将JsonReader的内容转为T
## JsonElement---->json元素
- JsonObject
    - members---->包含name-value pairs JsonElement
    - 提供创建获取元素
- JsonArray
    -  elements---->数组元素支持
    - 添加、获取、迭代元素
- JsonPrimitive
    - 8种基本类型与封装类型支持
- JsonNull
    - null对象支持
## TypeAdapter---->类型适配器
- 从JsonReader读取字符流转为T,将T利用JsonWriter转为字符流
- TypeAdapters---->提供基本类型的类型适配器,例如BOOLEAN、BYTE、LONG等类型适配器
- ReflectiveTypeAdapterFactory---->TypeAdapterFactory
- com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
    - com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField---->反射解析出来的字段信息
    - 利用注解转换Java中普通的POJO
- FieldNamingPolicy---->FieldNamingStrategy
    - 将字段名称按照策略进行转换,例如字段名称、首字母大写、小写下划线
## JsonWriter/JsonReader---->json流支持
- com.google.gson.stream.JsonWriter.value(java.lang.String)---->写字段值
- com.google.gson.stream.JsonReader.nextString---->读取字段值
## JsonToken---->json的标识符
## 注解支持---->SerializedName/Expose/JsonAdapter
- SerializedName用于支持序列化与反序列化的字段名称,会覆盖FieldNamingStrategy
- Expose用于表示字段是否应该序列化与反序列化
- JsonAdapter以注解的方式支持TypeAdapter
## TypeToken---->对java泛型擦除的补充
- 记录原生类型与参数化类型信息
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git
- com.undergrowth.google.gson.GsonDemo.main
- com.undergrowth.google.gson.writer.JsonWriterDemo.main
- com.undergrowth.google.gson.jsonadapter.UserJsonAdapter.main
- 详细讲解toJson与fromJson数据流
  - gson.toJson/fromJson
    - com.google.gson.Gson.toJson(java.lang.Object, java.lang.reflect.Type, com.google.gson.stream.JsonWriter)
      - com.google.gson.Gson.getAdapter(com.google.gson.reflect.TypeToken)---->查找适配器
        - candidate = factory.create(this, type)---->利用初始化的适配器工厂创建适配器
          - com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.create---->对于POJO,调用此方法
            - com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter.Adapter
            - 利用此read/wirte利用JsonWriter/JsonReader将T与json之间进行转换