package com.undergrowth.java.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;

/**
 * Description: TODO(这里用一句话描述这个类的作用) NAT------内外网地址转换 DNS------IP与域名的转换 IPV4----4分8字节十机制 IPV6----8分16字节十六进制
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年7月8日
 */
public class InternetAddress {

    @Test
    public void testInternetLocal() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        System.out.println(
            address.getHostName() + ":" + address.getHostAddress() + ":" + address.getAddress() + ":"
                + address.getCanonicalHostName());
    }

    @Test
    public void testInternetByName() throws UnknownHostException {
        InetAddress address = InetAddress.getByName("www.baidu.com");
        System.out.println(
            address.getHostName() + ":" + address.getHostAddress() + ":" + address.getAddress() + ":"
                + address.getCanonicalHostName());
    }

    @Test
    public void testInternetByAddress() throws UnknownHostException {
        byte[] addressIp = {119, 75, (byte) 218, 70};
        InetAddress address = InetAddress.getByAddress(addressIp);
        // 若通过ip反解析域名 找不到的话则使用ip地址
        System.out.println(
            address.getHostName() + ":" + address.getHostAddress() + ":" + address.getAddress() + ":"
                + address.getCanonicalHostName());
    }

    @Test
    public void testInternetSpamCheck() throws UnknownHostException {
        InetAddress address = InetAddress.getByName("sbl.spamhaus.org");
        // 若通过ip反解析域名 找不到的话则使用ip地址
        System.out.println(
            address.getHostName() + ":" + address.getHostAddress() + ":" + address.getAddress() + ":"
                + address.getCanonicalHostName());
    }

}
