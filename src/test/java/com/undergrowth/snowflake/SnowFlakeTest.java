package com.undergrowth.snowflake;

import java.text.ParseException;
import java.util.Set;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.Sets;
import org.junit.Test;

/**
 * snowflake是twitter开源的分布式ID生成算法，其核心思想是：一个long型的ID，使用其中41bit作为毫秒数，10bit作为机器编号，12bit作为毫秒内序列号。这个算法单机每秒内理论上最多可以生成1000*(2^12)，也就是400W的ID，完全能满足业务的需求。
 *
 * 借鉴snowflake的思想，结合各公司的业务逻辑和并发量，可以实现自己的分布式ID生成算法。
 *
 * 举例，假设某公司ID生成器服务的需求如下：
 *
 * （1）单机高峰并发量小于1W，预计未来5年单机高峰并发量小于10W （2）有2个机房，预计未来5年机房数量小于4个 （3）每个机房机器数小于100台 （4）目前有5个业务线有ID生成需求，预计未来业务线数量小于10个 （5）…
 *
 * 分析过程如下：
 *
 * （1）高位取从2016年1月1日到现在的毫秒数（假设系统ID生成器服务在这个时间之后上线），假设系统至少运行10年，那至少需要10年*365天*24小时*3600秒*1000毫秒=320*10^9，差不多预留39bit给毫秒数
 *
 * （2）每秒的单机高峰并发量小于10W，即平均每毫秒的单机高峰并发量小于100，差不多预留7bit给每毫秒内序列号
 *
 * （3）5年内机房数小于4个，预留2bit给机房标识
 *
 * （4）每个机房小于100台机器，预留7bit给每个机房内的服务器标识
 *
 * （5）业务线小于10个，预留4bit给业务线标识
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-11-23-11:33
 */
public class SnowFlakeTest {

    public double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    @Test
    public void test() {
        System.out.println(log(320 * Math.pow(10, 9), 2));
    }

    @Test
    public void testDistributeId() throws ParseException, InterruptedException {
        long start = DateUtils.parseDate("2016-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
        long addNum = 0;
        Set<Long> idSet = Sets.newHashSet();
        long timeStart = System.currentTimeMillis();
        for (int j = 0; j < 34; j++) {
            for (int i = 0; i < 3030; i++) {
                addNum = ((addNum + 1) & (~(-1L << 18)));
                long id = genID(start, addNum);
                //System.out.println(id);
                idSet.add(id);
            }
            // Thread.sleep(1);
        }
        System.out.println(System.currentTimeMillis() - timeStart);
        System.out.println(idSet.size());
    }

    private long genID(long start, long addNum) {
        long currentMis = System.currentTimeMillis() - start;
        long busCode = 1;
        long macHouse = 1;
        long mac = 1; //(mac << 13) |
        return (currentMis << 24) | (busCode << 20) | (macHouse << 18) | (addNum);
    }

}