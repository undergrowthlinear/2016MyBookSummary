package com.undergrowth.distribute.service.framework;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Description: TODO(这里用一句话描述这个类的作用) 通过代理将服务类、参数传递到服务端，进行远程方法调用
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年9月24日
 */
public class RpcImporter<S> {

    public S importer(final Class<?> serviceClass, final InetSocketAddress address) {
        return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(),
            new Class<?>[]{serviceClass.getInterfaces()[0]}, new InvocationHandler() {

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // TODO Auto-generated method stub
                    Socket client = null;
                    ObjectInputStream ois = null;
                    ObjectOutputStream oos = null;
                    try {
                        client = new Socket();
                        client.connect(address);
                        oos = new ObjectOutputStream(client.getOutputStream());
                        oos.writeUTF(serviceClass.getName());
                        oos.writeUTF(method.getName());
                        oos.writeObject(method.getParameterTypes());
                        oos.writeObject(args);
                        ois = new ObjectInputStream(client.getInputStream());
                        return ois.readObject();
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
            });
    }

}
