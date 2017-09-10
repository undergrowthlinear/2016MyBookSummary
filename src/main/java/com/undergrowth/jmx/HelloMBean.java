package com.undergrowth.jmx;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description HelloMBean
 * @date 2017-09-10-10:15
 */
public interface HelloMBean {
  public String getName();
  public void setName(String name);

  public void printHello();
  public void printHello(String whoName);
}
