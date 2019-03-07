package com.undergrowth.interview;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * 合并两链表,有序输出结果
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-03-07-16:32
 */
public class SimpleLinkedList {

    private static final int SIZE = 10;
    private static final int MAX_NUM = 10000;
    private static Random randomGenerator = new Random();

    public static void main(String[] args) {
        LinkedList<Integer> firstLinked = new LinkedList<>();
        fillLinkedData(firstLinked);
        LinkedList<Integer> secondLinked = new LinkedList<>();
        fillLinkedData(secondLinked);
        LinkedList<Integer> mergeLinkedResult = mergeLinked(firstLinked, secondLinked);
        //
        Collections.sort(mergeLinkedResult);
        //displayLinkedResult(mergeLinkedResult);
    }

    /**
     * 显示链表结果
     *
     * @param result 待显示链表
     */
    private static void displayLinkedResult(LinkedList<Integer> result) {
        for (Integer num :
            result) {
            System.out.print(num + "\t");
        }
    }

    /**
     * 合并链表
     *
     * @param firstLinked 待合并链表
     * @param secondLinked 待合并链表
     * @return 合并后链表
     */
    private static LinkedList<Integer> mergeLinked(LinkedList<Integer> firstLinked, LinkedList<Integer> secondLinked) {
        LinkedList<Integer> mergeResult = new LinkedList<>();
        mergeResult.addAll(firstLinked);
        mergeResult.addAll(secondLinked);
        return mergeResult;
    }

    /**
     * 填充数据
     *
     * @param unfillLinked 待填充链表
     */
    private static void fillLinkedData(LinkedList<Integer> unfillLinked) {
        for (int i = 0; i < SIZE; i++) {
            unfillLinked.add(randomGenerator.nextInt(MAX_NUM));
        }
    }

}