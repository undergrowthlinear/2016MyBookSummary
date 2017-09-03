package com.undergrowth.distribute.service.framework;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RpcTest {

  public static void main(String[] args) {
    new Thread(new Runnable() {

      @Override
      public void run() {
        // TODO Auto-generated method stub
        try {
          RpcExporter.exporter("localhost", 8088);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }).start();
    //
    while (true) {
      RpcImporter<EchoSercive> rpcImporter = new RpcImporter<>();
      EchoSercive echoSercive = rpcImporter.importer(EchoServiceImpl.class,
          new InetSocketAddress("localhost", 8088));
      System.out.println(echoSercive.echo("ping"));
      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
