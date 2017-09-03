package com.undergrowth.junit;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description ingore
 * @date 2017-09-03-10:21
 */
public class IgnoreTest {

  @Ignore("not ready yet")
  @Test
  public void something() {
  }

}
