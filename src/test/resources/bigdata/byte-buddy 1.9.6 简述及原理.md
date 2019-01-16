# byte-buddy 1.9.6 简述及原理1
## 概述
- 参考
    - https://github.com/raphw/byte-buddy
    - https://github.com/diguage/byte-buddy-tutorial
    - https://blog.csdn.net/qyongkang/article/details/7799603 -javaagent参数使用
## 核心Code
- ByteBuddy
    - 流式api方式的入口类
    - 提供Subclassing/Redefining/Rebasing方式改写字节码
    - 所有的操作依赖DynamicType.Builder进行,创建不可变的对象
- ElementMatchers(ElementMatcher)
    - 提供一系列的元素匹配的工具类(named/any/nameEndsWith等等)
    - ElementMatcher(提供对类型、方法、字段、注解进行matches的方式,类似于Predicate)
        - Junction对多个ElementMatcher进行了and/or操作
- Implementation(用于提供动态方法的实现)
    - FixedValue(方法调用返回固定值)
    - MethodDelegation(方法调用委托,支持以下两种方式)
        - Class的static方法调用
        - object的instance method方法调用
- DynamicType(动态类型,所有字节码操作的开始,非常值得关注)
    - Unloaded(动态创建的字节码还未加载进入到虚拟机,需要类加载器进行加载)
    - Loaded(已加载到jvm中后,解析出Class表示)
    - Default(DynamicType的默认实现,完成相关实际操作)
- Builder(用于创建DynamicType,相关接口以及实现后续待详解)
    - MethodDefinition
    - FieldDefinition
    - AbstractBase
- AgentBuilder(java agent的操作入口,后续详解)
- Transformer(对实例进行转换操作)
## 示例(https://github.com/undergrowthlinear/byte-buddy-test)
### HelloWorld
- 完整示例代码: https://github.com/undergrowthlinear/byte-buddy-test/blob/master/src/test/java/com/undergrowth/ByteBuddyHelloWorldTest.java
- 核心解释
```
 Class<?> dynamicType = new ByteBuddy()
            .subclass(Object.class)
            .method(ElementMatchers.named("toString"))
            .intercept(FixedValue.value("Hello World!"))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
        String str = dynamicType.newInstance().toString();
        System.out.println(str);
        Assert.assertEquals("toString方法改写错误", str, "Hello World!");
```
- dynamicType继承Object,重写toString方法,将该方法的返回值固定为Hello World!,利用当前类的类加载器,将字节码加载到虚拟机中,形成Class字节码表示
### Agent 示例
- 完整示例代码:https://github.com/undergrowthlinear/byte-buddy-test/tree/master/src/main/java/com/undergrowth/agent
- 1.编写agnet核心
```
public static void premain(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default()
            .type(ElementMatchers.isAnnotatedWith(ToString.class))
            .transform(new AgentBuilder.Transformer() {
                @Override
                public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                    return builder.method(named("toString"))
                        .intercept(FixedValue.value("transformed"));
                }
            }).installOn(instrumentation);
    }
```
- 拦截被ToString注解标注的类,对其toString方法进行拦截后,返回固定值
- 2.添加maven-shade-plugin插件,添加premain
- 3.com.undergrowth.AgentToStringMain#main 启动 可通过是否添加 -javaagent参数 查看代理机制有无生效
### Agent 类似于切面示例(通过aspectj感觉更好)
- 完整示例代码:https://github.com/undergrowthlinear/byte-buddy-test/tree/master/src/main/java/com/undergrowth/time
- 1.编写agnet核心
```
public static void premain(String arguments,
        Instrumentation instrumentation) {
        new AgentBuilder.Default()
            .type(ElementMatchers.nameEndsWith("Timed"))
            .transform((builder, type, classLoader, module) ->
                builder.method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(TimingInterceptor.class))
            ).installOn(instrumentation);
    }
```
- 拦截后缀名为Timed的类,对其任意方法进行拦截后,打印方法调用时间
- 2.添加maven-shade-plugin插件,添加premain
- 3.com.undergrowth.time.TimingInterceptorMain#main 启动 可通过是否添加 -javaagent参数 查看代理机制有无生效
```
hello world
public void com.undergrowth.time.AgentInterceptorTimed.hello() took 1
hello world helloSleep
public void com.undergrowth.time.AgentInterceptorTimed.helloSleep() throws java.lang.InterruptedException took 1453
```
### 方法委托方式
- 完整示例代码:https://github.com/undergrowthlinear/byte-buddy-test/blob/master/src/test/java/com/undergrowth/MethodInterceptorTest.java
- com.undergrowth.MethodInterceptorTest#interceptorSimpleTest
    - 简单模式(将Function的apply调用委托给javabean的方法调用)
```
Class<? extends java.util.function.Function> dynamicType = new ByteBuddy()
            .subclass(java.util.function.Function.class)
            .method(ElementMatchers.named("apply"))
            .intercept(MethodDelegation.to(new GreetingInterceptorSimple()))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
```
- com.undergrowth.MethodInterceptorTest#interceptorComplexTest
- com.undergrowth.MethodInterceptorTest#interceptorComplex2Test
    - 复杂模式(兼容所有情况/获取到原有调用方法以及所有的方法调用参数)
```
@RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
        @Origin Method method) {
        // intercept any method of any signature
        System.out.println("called:" + method.getName());
        if (allArguments != null && allArguments.length > 0) {
            return "Hello from " + allArguments[0];
        }
        return "Hello from " + method.getName();
    }
```