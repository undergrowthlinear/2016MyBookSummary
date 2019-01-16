# byte-buddy 1.9.6 简述及原理2-更多实例
## 概述
- 参考
    - https://blog.csdn.net/undergrowth/article/details/86493336
    - https://www.infoq.cn/article/Easily-Create-Java-Agents-with-ByteBuddy
- 本文主要介绍创建字节码的不同方式以及使用agent实现安全框架原型(还需加上类似ThreadLocal ContextSecurity 机制进行融合,业务上才可完整应用)
## 示例(https://github.com/undergrowthlinear/byte-buddy-test)
### CreateByteCodeSomeTest
- 完整示例代码: https://github.com/undergrowthlinear/byte-buddy-test/blob/master/src/test/java/com/undergrowth/CreateByteCodeSomeTest.java
- 核心解释
```
 DynamicType.Unloaded unloadedDynamicType;
 
     @Before
     public void before() {
         unloadedDynamicType = new ByteBuddy(ClassFileVersion.JAVA_V8)
             .subclass(Object.class)
             .name("test.Test")
             .implement(First.class)
             .implement(Second.class)
             .method(named("qux")).intercept(DefaultMethodCall.prioritize(First.class))
             .make();
     }
 
     @Test
     public void implemetentTest() throws IOException {
         unloadedDynamicType.saveIn(new File("./target"));
     }
 
     @Test
     public void implemetent2Test() throws IOException, IllegalAccessException, InstantiationException {
         Class<First> cls = unloadedDynamicType.load(getClass().getClassLoader()).getLoaded();
         String str = cls.newInstance().qux();
         System.out.println(str);
         Assert.assertEquals("bar方法改写错误", str, "FOO");
     }
 
     @Test
     public void createMultiTest() throws IllegalAccessException, InstantiationException {
         Foo dynamicFoo = new ByteBuddy()
             .subclass(Foo.class)
             .method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("One!"))
             .method(named("foo")).intercept(FixedValue.value("Two!"))
             .method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("Three!"))
             .make()
             .load(getClass().getClassLoader())
             .getLoaded()
             .newInstance();
         System.out.println(dynamicFoo.bar());
         System.out.println(dynamicFoo.foo());
         System.out.println(dynamicFoo.foo("a"));
         Assert.assertEquals("bar方法改写错误", dynamicFoo.bar(), "One!");
         Assert.assertEquals("foo方法改写错误", dynamicFoo.foo(), "Two!");
         Assert.assertEquals("foo方法改写错误", dynamicFoo.foo("a"), "Three!");
     }
```
- implemetentTest与implemetent2Test用于展示创建新的字节码继承Object,实现First/Second接口,同时制定父类方法调用优先级,查看生成的字节码信息
- createMultiTest用于展示对不同的方法进行转换
### Agent 安全框架原型示例
- 完整示例代码:https://github.com/undergrowthlinear/byte-buddy-test/tree/master/src/main/java/com/undergrowth/secure
- 1.编写agnet核心
```SecurityAgent
 public static void premain(String arg, Instrumentation inst) {
        new AgentBuilder.Default()
            .type(ElementMatchers.any())
            .transform((builder, type, classLoader, module) -> builder
                .method(ElementMatchers.isAnnotatedWith(Secured.class))
                .intercept(MethodDelegation.to(new SecurityInterceptor())
                    .andThen(SuperMethodCall.INSTANCE)))
            .installOn(inst);
    }
```
```SecurityInterceptor
static String user = "ADMIN";

    @RuntimeType
    public void intercept(@AllArguments Object[] allArguments, @Origin Method method) {
        if (!method.getAnnotation(Secured.class).user().equals(user)) {
            throw new IllegalStateException("Wrong user");
        }
        System.out.println("pass security check method:" + method.getName());
    }
```
- 
- 2.添加maven-shade-plugin插件,添加premain
- 3.com.undergrowth.secure.SecurityAgentMain#main 启动 可通过是否添加 -javaagent参数 查看代理机制有无生效
```
pass security check method:doSensitiveAction
dangerous doSensitiveAction
Exception in thread "main" java.lang.IllegalStateException: Wrong user
	at com.undergrowth.secure.SecurityInterceptor.intercept(SecurityInterceptor.java:20)
	at com.undergrowth.secure.Service1.deleteDataBase(Service1.java)
	at com.undergrowth.secure.SecurityAgentMain.main(SecurityAgentMain.java:17)
```