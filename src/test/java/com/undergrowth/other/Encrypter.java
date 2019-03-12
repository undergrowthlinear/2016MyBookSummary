package com.undergrowth.other;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密器
 *
 * @author zhaoxuanzhang, 2009-9-28
 *
 * $LastChangedDate$ $Author$
 */
public class Encrypter {

    /**
     * md5加密
     *
     * @param data 原始密码
     * @return 加密后的密码的hex字符串
     * @author zhaoxuanzhang, 2009-9-28
     */
    public static String md5Encrypt(String data) {
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte[] digest = digester.digest(data.getBytes());
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < digest.length; ++i) {
                String digit = Integer.toHexString(digest[i] >= 0 ? digest[i] : 256 + digest[i]).toUpperCase();
                if (digit.length() == 1) {
                    digit = "0" + digit;
                }
                b.append(digit);
            }
            return b.toString();
        } catch (NoSuchAlgorithmException e) {
            return data;
        }
    }

    /**
     * SHA-256加密
     *
     * @param data 原始密码
     * @return 加密后的密码的hex字符串
     * @author zhaoxuanzhang, 2009-9-28
     */
    public static String sha256Encrypt(String data) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            byte[] digest = digester.digest(data.getBytes());
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < digest.length; ++i) {
                String digit = Integer.toHexString(digest[i] >= 0 ? digest[i] : 256 + digest[i]).toUpperCase();
                if (digit.length() == 1) {
                    digit = "0" + digit;
                }
                b.append(digit);
            }
            return b.toString();
        } catch (NoSuchAlgorithmException e) {
            return data;
        }
    }

    public static void main(String[] args) {
//			System.out.println("MD5:"+Encrypter.sha256Encrypt("jiayen"));
        System.out.println("MD5:" + Encrypter.md5Encrypt("888888").substring(8, 24));
    }
}
