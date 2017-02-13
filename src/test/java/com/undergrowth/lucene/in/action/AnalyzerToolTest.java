package com.undergrowth.lucene.in.action;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 分析器测试类
 * @date 2017-02-13-21:15
 */
public class AnalyzerToolTest {

    private Logger logger = LoggerFactory.getLogger(AnalyzerToolTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDisTokenStreamInfo() {
        String text = "how are you thank you";
        AnalyzerTool analyzerTool = new AnalyzerTool();
        Analyzer st = new StandardAnalyzer(Version.LUCENE_35);
        Analyzer sa = new StopAnalyzer(Version.LUCENE_35);
        Analyzer sia = new SimpleAnalyzer(Version.LUCENE_35);
        Analyzer ws = new WhitespaceAnalyzer(Version.LUCENE_35);
        analyzerTool.disTokenStreamInfo(text, st);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, sa);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, sia);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, ws);
        logger.info("--------分割线--------");
    }

    @Test
    public void testDisTokenStreamInfo2() {
        String text = "what's you name i'm is lucy,my phone is 123456789";
        AnalyzerTool analyzerTool = new AnalyzerTool();
        Analyzer st = new StandardAnalyzer(Version.LUCENE_35);
        Analyzer sa = new StopAnalyzer(Version.LUCENE_35);
        Analyzer sia = new SimpleAnalyzer(Version.LUCENE_35);
        Analyzer ws = new WhitespaceAnalyzer(Version.LUCENE_35);
        analyzerTool.disTokenStreamInfo(text, st);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, sa);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, sia);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, ws);
        logger.info("--------分割线--------");
    }

    @Test
    public void testDisTokenStreamInfo3() {
        String text = "你的名字叫什么,我的名字叫小明";
        AnalyzerTool analyzerTool = new AnalyzerTool();
        Analyzer st = new StandardAnalyzer(Version.LUCENE_35);
        Analyzer sa = new StopAnalyzer(Version.LUCENE_35);
        Analyzer sia = new SimpleAnalyzer(Version.LUCENE_35);
        Analyzer ws = new WhitespaceAnalyzer(Version.LUCENE_35);
        analyzerTool.disTokenStreamInfo(text, st);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, sa);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, sia);
        logger.info("--------分割线--------");
        analyzerTool.disTokenStreamInfo(text, ws);
        logger.info("--------分割线--------");
    }

}
