package com.undergrowth.jmx;

import com.sun.jdmk.comm.HtmlAdaptorServer;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

/**
 * @author zhangwu http://blog.csdn.net/vking_wang/article/details/8668743
 * @version 1.0.0
 * @description HelloAgent
 * @date 2017-09-10-10:15
 */
public class HelloAgent {

    public static void main(String[] args) throws Exception {
        //先创建了一个MBeanServer，用来做MBean的容器
        MBeanServer server = MBeanServerFactory.createMBeanServer();
//      MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName helloName = new ObjectName("alpha:name=HelloWorld");
        Hello hello = new Hello();
        //将Hello这个类注入到MBeanServer中，注入需要创建一个ObjectName类
        server.registerMBean(hello, helloName);

        //创建一个AdaptorServer，这个类将决定MBean的管理界面，这里用最普通的Html型界面。AdaptorServer其实也是一个MBean。
        // alpha:name=HelloWorld的名字是有一定规则的，格式为：“域名:name=MBean名称”，域名和MBean名称都可以任意取。
        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
        server.registerMBean(adapter, adapterName);
        adapter.start();
        System.out.println("start.....");
    }
}
