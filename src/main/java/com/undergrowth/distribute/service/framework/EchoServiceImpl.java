package com.undergrowth.distribute.service.framework;

public class EchoServiceImpl implements EchoSercive {

  @Override
  public String echo(String ping) {
    // TODO Auto-generated method stub
    return ping != null ? ping + "--> i am ok" : "i am ok";
  }

}
