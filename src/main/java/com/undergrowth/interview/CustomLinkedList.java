package com.undergrowth.interview;

import java.util.Random;

/**
 * 自定义实现链表操作
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-03-07-16:55
 */
public class CustomLinkedList {

    private static final int SIZE = 10;
    private static final int MAX_NUM = 10000;
    private static Random randomGenerator = new Random();

    private static class ListNode<E> {
        E e;
        ListNode<E> prev;
        ListNode<E> next;

        public ListNode(E e, ListNode<E> prev, ListNode<E> next) {
            this.e = e;
            this.prev = prev;
            this.next = next;
        }
    }

    /**
     * 自定义链表
     */
    private static class CustomLinked<E> {

        int size;
        // 记录第一个节点
        ListNode<E> first;
        // 记录最后一个节点
        ListNode last;

        public CustomLinked() {

        }

        /**
         * 将新数据添加到链表末尾
         *
         * @param e 数据
         */
        public void add(E e) {
            //  当前节点
            ListNode currNode = last;
            // 将新节点附加到当前节点后面
            ListNode newNode = new ListNode(e, currNode, null);
            last = newNode;
            if (currNode == null) { // 判断是否还没有数据
                first = newNode;
            } else {
                currNode.next = newNode;
            }
            size++;
        }

        public void addAll(CustomLinked<E> c) {
            if (c.size == 0) {
                return;
            }
            if (size == 0) { // 若无数据 直接链表赋值
                first = c.first;
                last = c.last;
                size = c.size;
                return;
            }
            // 链表前后连接
            last.next = c.first;
            last = c.last;
            size += c.size;
        }
    }

    public static void main(String[] args) {
        CustomLinked<Integer> firstLinked = new CustomLinked<>();
        fillCustomLinkedData(firstLinked);
        CustomLinked<Integer> secondLinked = new CustomLinked<>();
        fillCustomLinkedData(secondLinked);
        CustomLinked<Integer> mergeLinkedResult = mergeCustomLinked(firstLinked, secondLinked);
        mergeLinkedResult = resortCustomLinked(mergeLinkedResult);
        //displayLinkedResult(mergeLinkedResult);
    }

    /**
     * 采用快速排序实现 保障时间复杂度为 o(nlog n) 迭代分而治之
     *
     * @param mergeLinkedResult 待排序链表
     */
    private static CustomLinked<Integer> resortCustomLinked(CustomLinked<Integer> mergeLinkedResult) {
        quickSortLink(mergeLinkedResult.first, null);
        return mergeLinkedResult;
    }

    private static void quickSortLink(ListNode<Integer> startNode, ListNode<Integer> endNode) {
        if (startNode != endNode) {
            // 先找到中间点
            ListNode midNode = findMid(startNode, endNode);
            // 左边排序
            quickSortLink(startNode, midNode);
            // 右边排序
            quickSortLink(midNode.next, endNode);
        }
    }

    /**
     * 利用p q 分别移动 找到中间点 保障 p之前的数据小于key p q之间数据大于key
     *
     * @param startNode 其实节点
     * @param endNode 结束节点
     * @return 中间节点
     */
    private static ListNode findMid(ListNode<Integer> startNode, ListNode<Integer> endNode) {
        // 利用p q 分别移动 找到中间点
        // 保障 p之前的数据小于key p q之间数据大于key
        int key = startNode.e;
        ListNode<Integer> p = startNode, q = startNode.next;
        while (q != endNode) { // 是否到末尾
            if (key > q.e) { //保障key之前的都小于它
                p = p.next;
                int tmp = p.e;
                p.e = q.e;
                q.e = tmp;
            }
            // 向后移动
            q = q.next;
        }
        // 交换key中间点的值
        if (p != startNode) {
            int tmp = startNode.e;
            startNode.e = p.e;
            p.e = tmp;
        }
        return p;
    }


    /**
     * 显示链表结果
     *
     * @param result 待显示链表
     */
    private static void displayLinkedResult(CustomLinked<Integer> result) {
        ListNode currNode = result.first;
        for (int i = 0; ; i++) {
            if (currNode == null) {
                break;
            }
            System.out.print(currNode.e + "\t");
            currNode = currNode.next;
        }
    }

    /**
     * 合并链表
     *
     * @param firstLinked 待合并链表
     * @param secondLinked 待合并链表
     * @return 合并后链表
     */
    private static CustomLinked<Integer> mergeCustomLinked(CustomLinked<Integer> firstLinked, CustomLinked<Integer> secondLinked) {
        CustomLinked<Integer> mergeResult = new CustomLinked<>();
        mergeResult.addAll(firstLinked);
        mergeResult.addAll(secondLinked);
        return mergeResult;
    }

    /**
     * 填充数据
     *
     * @param unfillLinked 待填充链表
     */
    private static void fillCustomLinkedData(CustomLinked<Integer> unfillLinked) {
        for (int i = 0; i < SIZE; i++) {
            unfillLinked.add(randomGenerator.nextInt(MAX_NUM));
        }
    }

}