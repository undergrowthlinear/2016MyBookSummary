package com.undergrowth.interview;

/**
 * 对比利用工具类与自己实现的性能
 *
 * 循环100000耗时如下: 自定义链表运行100000,耗时:807 工具类链表运行100000,耗时:1448 原因分析: 自定义的采用快速排序,同时无业务数据转换,直接操作链表,导致较快 工具类采用二分查找,同时还有空间损耗,可能是通用型的原因,导致
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-03-07-18:09
 */
public class CompareSimpleAndCustom {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int loopNum = 100000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < loopNum; i++) {
            CustomLinkedList.main(null);
        }
        // 方便查看 可注释输出
        System.out.println("自定义链表运行" + loopNum + ",耗时:" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < loopNum; i++) {
            SimpleLinkedList.main(null);
        }
        System.out.println("工具类链表运行" + loopNum + ",耗时:" + (System.currentTimeMillis() - start));
    }

}