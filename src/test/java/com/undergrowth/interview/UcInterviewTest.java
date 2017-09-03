package com.undergrowth.interview;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description uc面试题目
 * @date 2017-02-08-8:55
 */
public class UcInterviewTest {

  @Test
  public void charTest() {
    char a = '你';
    System.out.println(a);
  }

  @Test
  public void concurrentTest() {

  }

  @Test
  public void numTest() {
    List<Integer> source = new ArrayList<Integer>();
    List<Integer> out = new ArrayList<Integer>();
    for (int i = 1; i <= 30; i++) {
      source.add(i);
    }
    int index = 0;
    while (source.size() > 0) {
      index = (index + 4) % source.size();
      out.add(source.get(index));
      source.remove(index);
    }
    for (int i = 0; i < out.size(); i++) {
      System.out.print(out.get(i) + " ");
    }
  }

}
