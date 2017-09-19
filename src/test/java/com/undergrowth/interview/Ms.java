package com.undergrowth.interview;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 面试题目测试
 * @date 2017-02-09-9:09
 */
public class Ms {

  @Test
  public void floatTest() {
    float f = 3.4f;
    System.out.println(f);
  }

  @Test
  public void shortTest() {
    short s = 1;
    s = (short) (s + 1);
    s += 1;
    System.out.println(s);
    int i = s;
    System.out.println(i);
  }

  @Test
  public void autoTest() {
    Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;
    System.out.println(f1 == f2);
    System.out.println(f3 == f4);
  }

  @Test
  public void andTest() {
    String s = null;
    //短路
    if (s != null && s.equals("a")) {
      System.out.println(s);
    } else {
      System.out.println("b");
    }
    //不短路
    if (s != null & s.equals("a")) {
      System.out.println(s);
    } else {
      System.out.println("b");
    }
  }

  @Test
  public void internTest() {
    String s1 = new StringBuilder("go")
        .append("od").toString();
    System.out.println(s1.intern() == s1);
    String s2 = new StringBuilder("ja")
        .append("va").toString();
    System.out.println(s2.intern() == s2);
  }

  @Test
  public void internqTest() {
    String s1 = new StringBuilder("go")
        .append("od").toString();
    System.out.println(s1.intern() == s1);
    String s2 = new StringBuilder("ja")
        .append("vaq").toString();
    System.out.println(s2.intern() == s2);
  }

  @Test
  public void intern1Test() {
    String s = new String("muuyukku");
    String s3 = s.intern();
    String s1 = "muuyukku";
    System.out.println(s == s1);
    System.out.println(s3 == s1);
  }

  @Test
  public void intern2Test() {
    String s = new String("muuyukku");
    String s1 = "muuyukku";
    s.intern();
    System.out.println(s == s1);
  }

  @Test
  public void intern3Test() {
    String s = new String("1");
    String s2 = "1";
    s.intern();
    System.out.println(s == s2);

    String s3 = new String("1") + new String("1");
    String s4 = "11";
    s3.intern();
    System.out.println(s3 == s4);
  }

  @Test
  public void intern4Test() {
    String s = new String("1");
    String s2 = "1";
    s.intern();
    System.out.println(s == s2);

    String s3 = new String("1") + new String("1");
    s3.intern();
    String s4 = "11";
    System.out.println(s3 == s4);
  }

  @Test
  public void stackTest() {
    Stack stack = new Stack();
  }

  @Test
  public void splitTest() {
    String str = "a,b,c,,";
    String[] ary = str.split(",");
    System.out.println(ary.length);
  }

  @Test
  public void arrayTest() {
    String[] str = new String[]{"a", "b"};
    List list = Arrays.asList(str);
    list.add("c");
  }

  @Test
  public void threadPoolTest() {
    //ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor();
  }


}
