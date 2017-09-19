package com.undergrowth.distribute.service.framework;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Description: TODO(服务端发布者)
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年9月24日
 */
public class RpcExporter {

  static Executor executor = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public static void exporter(String host, int port) throws IOException {
    ServerSocket serverSocket = new ServerSocket();
    serverSocket.bind(new InetSocketAddress(host, port));
    try {
      while (true) {
        executor.execute(new ExporterTask(serverSocket.accept()));
      }
    } finally {
      // TODO: handle finally clause
      serverSocket.close();
    }
  }

}
