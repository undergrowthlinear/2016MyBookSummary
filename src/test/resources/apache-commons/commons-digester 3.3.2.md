# apache-comnons系列之commons-digester 3.3.2 学习笔记
## 概述
- 参考
  - http://www.blogjava.net/tw-ddm/archive/2006/07/20/59299.html
  - http://macken.iteye.com/blog/1379181
## Digester---->入口解析
- Rules rules---->存放预先注册的规则与模式
- 利用堆存放创建的对象stack
- 利用ContentHandler在startDocument、endDocument进行相关的初始化与清理工作
- 利用ContentHandler在startElement、endElement的时候查找合适的rule进行begin/end操作
- org.apache.commons.digester3.Digester#parse(java.lang.String)
## Rule---->规则
- ObjectCreateRule---->Rule
- SetPropertiesRule---->Rule
- SetNextRule---->AbstractMethodRule---->Rule
## 测试
- org.apache.commons.digester3.examples.api.catalog.Main#main