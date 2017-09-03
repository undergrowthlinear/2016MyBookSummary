package com.undergrowth.junit;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description before
 * @date 2017-09-03-10:25
 */
public class BeforeTest {

  ArrayList empty;

  @Before
  public void initialize() {
    empty = new ArrayList();
  }

  @Test
  public void size() {
    Assert.assertNotNull(empty);
  }

  @Test
  public void remove() {

  }

  @After
  public void deleteOutputFile() {

  }
}
