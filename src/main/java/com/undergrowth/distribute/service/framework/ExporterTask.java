package com.undergrowth.distribute.service.framework;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Description: TODO(这里用一句话描述这个类的作用) 接收客户端传递的类/方法/参数类型/参数值,利用反射进行调用
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年9月24日
 */
public class ExporterTask implements Runnable {

  Socket client = null;

  public ExporterTask(Socket accept) {
    // TODO Auto-generated constructor stub
    this.client = accept;
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    try {
      oos = new ObjectOutputStream(client.getOutputStream());
      ois = new ObjectInputStream(client.getInputStream());
      String interfaceName = ois.readUTF();
      Class<?> serviceName = Class.forName(interfaceName);
      String methodName = ois.readUTF();
      Class<?>[] parameterTypes = (Class<?>[]) ois.readObject();
      Object[] arguments = (Object[]) ois.readObject();
      Method method = serviceName.getMethod(methodName, parameterTypes);
      Object result = method.invoke(serviceName.newInstance(), arguments);
      oos.writeObject(result);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      // TODO: handle finally clause
      if (oos != null) {
        try {
          oos.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      if (ois != null) {
        try {
          ois.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      if (client != null) {
        try {
          client.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

}
