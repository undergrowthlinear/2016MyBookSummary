package com.undergrowth;

/**
 * jvm option和program arguments的区别
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-07-17-14:35
 */
public class JvmOptionAndProgram {

    /**
     * 通过Run Configurations(Debug Configurations)->Arguments里填写program arguments和VM arguments 参数之间用空格隔开，其本质类似于执行命令 java -DvmParam1=vmParam1
     * -DvmParam2=vmParam2 TestInputParam 1 2 program arguments存储在String[] args里 VM arguments设置的是虚拟机的属性，要传给java虚拟机的。KV形式存储的，通过System.getProperty("PropertyName")获取
     */

    public static void main(String[] args) {

        System.out.println("program arguments:");
        if (args == null || args.length == 0) {
            System.out.println("\t" + "no input params");
        } else {
            for (String arg : args) {
                System.out.println("\t" + arg);
            }
        }

        System.out.println("VM arguments:");
        String vmParam1 = "vmParam1";
        String vmParam2 = "vmParam2";
        System.out.println("\tName：" + vmParam1 + ",Value:" + System.getProperty(vmParam1));
        System.out.println("\tName：" + vmParam2 + ",Value:" + System.getProperty(vmParam2));

        System.out.println("Default VM arguments:");
        System.out.println("java_vendor:" + System.getProperty("java.vendor"));
        System.out.println("java_vendor_url:"
            + System.getProperty("java.vendor.url"));
        System.out.println("java_home:" + System.getProperty("java.home"));
        System.out.println("java_class_version:"
            + System.getProperty("java.class.version"));
        System.out.println("java_class_path:"
            + System.getProperty("java.class.path"));
    }

}