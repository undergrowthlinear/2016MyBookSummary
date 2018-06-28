package com.undergrowth.oom.opti;

/**
 * UnableNativeThread
 * win7由于线程模型关系 不会抛出异常 但是系统会卡死/甚至重启
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-06-28-17:25
 */
public class UnableNativeThread {

    public static void main(String args[]) throws Exception {
        while(true){
            new Thread(new Runnable(){
                public void run() {
                    try {
                        Thread.sleep(10000000);
                    } catch(InterruptedException e) { }
                }
            }).start();
        }
    }

}