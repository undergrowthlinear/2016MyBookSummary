package com.undergrowth.java.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Description: TODO(这里用一句话描述这个类的作用) URI----资源标识符 模式:模式的特定部分 模式----ftp/file/telnet/http/data/rmi/mailto/magnet/doc
 * 特定部分----授权机构/资源路径?查询参数 eg:模式://授权机构/资源路径?查询参数 eg:http://china.huanqiu.com/article/2016-07/9145823.html?from=bdwz
 * 模式----http 授权机构----china.huanqiu.com 资源路径----/article/2016-07/9145823.html 查询参数----from=bdwz
 * URL----资源定位符,既能标示资源又能定位资源 协议://用户信息@主机:端口/路径?查询参数#片段 eg:http://china.huanqiu.com/article/2016-07/9145823.html?from=bdwz
 * 相对URL(站内跳转)和绝对URL(站外跳转)
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年7月8日
 */
public class UriUrl {

  @Test
  public void testSupportProtocol() {
    testProtocol("http://www.baidu.com");
    testProtocol("https://www.baidu.com");
    testProtocol("mailto:undergrowth@126.com");
    testProtocol("telnet://dibner.poly.edu/");
    testProtocol("file:///d:");
    testProtocol("jdbc:mysql://localhost:3306/test");
    testProtocol("rmi://ibiblio.org/RenderEngine");
    testProtocol("nfs://utopia.poly.edu/usr/tmp/");
  }

  private void testProtocol(String url) {
    // TODO Auto-generated method stub
    try {
      URL u = new URL(url);
      System.out.println(u.getProtocol() + " is supported");
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      String protocol = url.substring(0, url.indexOf(':'));
      System.out.println(protocol + " is not supported");
    }
  }

  @Test
  public void testUrlCreate() {
    try {
      URL url = new URL("http", "mp.weixin.qq.com/",
          "/s?__biz=MjM5NTEyNjUwOA==&mid=2653014216&idx=1&sn=08f3c563ad1ef2a0f63a0f74c287775b&scene=1&srcid=0708Lv8lkkTfVqWEwvSvLykJ#wechat_redirect");
      disUrl(url);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private void disUrl(URL url) {
    // TODO Auto-generated method stub
    List aList = new ArrayList<>();
    aList.add(url.getProtocol());
    aList.add(url.getAuthority());
    aList.add(url.getUserInfo());
    aList.add(url.getHost());
    aList.add(url.getPort());
    aList.add(url.getFile());
    aList.add(url.getPath());
    aList.add(url.getQuery());
    aList.add(url.getRef());
    System.out.println(aList);
  }

  @Test
  public void testUrlConnection() {
    try {
      URL url = new URL("http://www.baidu.com");
      try (InputStream iStream = url.openStream()) {
        int c;
        while (((c = iStream.read())) != -1) {
          System.out.write(c);
        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Test
  public void testGetContent() {
    try {
      URL url = new URL("http://www.baidu.com");
      Class<?>[] types = new Class[3];
      types[0] = String.class;
      types[1] = Reader.class;
      types[2] = InputStream.class;
      Object object = url.getContent(types);
      if (object instanceof String) {
        System.out.println("String---->" + object);
      } else if (object instanceof Reader) {
        int c;
        Reader reader = (Reader) object;
        System.out.println("Reader---->");
        while (((c = reader.read())) != -1) {
          System.out.print((char) c);
        }
      } else if (object instanceof InputStream) {
        int c;
        System.out.println("InputStream---->");
        InputStream iStream = (InputStream) object;
        while (((c = iStream.read())) != -1) {
          System.out.write(c);
        }
      } else {
        System.out.println("Error : unexpected type");
      }
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void testTrans() {
    try {
      URL url = new URL("http://www.baidu.com");
      System.out.println(url.toString());
      System.out.println(url.toExternalForm());
      System.out.println(url.toURI());
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
