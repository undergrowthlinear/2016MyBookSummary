package com.undergrowth.lucene.in.action;

import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description tika解析类
 * @date 2017-02-13-21:19
 */
public class TikaParserTest {

    private Logger logger = LoggerFactory.getLogger(TikaParserTest.class);

    TikaParser parser = null;
    String filePathNull = "";
    String filePathXml = "tika_test_file.xml";
    String filePathJson = "tika_test_file.json";
    String filePathCsv = "tika_test_file.csv";
    String filePathHtml = "tika_test_file.html";
    String filePathDoc = "tika_test_file.doc";
    String filePath = "tika_test_file.xml";

    @Before
    public void setUp() throws Exception {
        parser = new TikaParser();
        filePath = ClassLoader.getSystemResource(filePath).getPath();
        filePathXml = ClassLoader.getSystemResource(filePathXml).getPath();
        filePathJson = ClassLoader.getSystemResource(filePathJson).getPath();
        filePathCsv = ClassLoader.getSystemResource(filePathCsv).getPath();
        filePathHtml = ClassLoader.getSystemResource(filePathHtml).getPath();
        filePathDoc = ClassLoader.getSystemResource(filePathDoc).getPath();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 抛出异常
     */
    @Test
    public void testParseTikaStringFilePathNull() {
        // logger.info(parser.parseTikaString(filePathNull));
        assertNull(parser.parseTikaString(filePathNull));
    }

    /**
     * 抛出异常
     */
    @Test
    public void testParseFileToStringFilePathNull() {
        // logger.info(parser.parseFileToString(filePathNull));
        assertNull(parser.parseFileToString(filePathNull));
    }

    @Test
    public void testParseTikaStringFilePath() {
        logger.info(parser.parseTikaString(filePath));
    }

    @Test
    public void testParseFileToStringFilePath() {
        logger.info(parser.parseFileToString(filePath));
    }

    @Test
    public void testParseFileToStringFilePathXml() {
        logger.info(parser.parseFileToString(filePathXml));
    }

    @Test
    public void testParseFileToStringFilePathJson() {
        logger.info(parser.parseFileToString(filePathJson));
    }

    @Test
    public void testParseFileToStringFilePathCsv() {
        logger.info(parser.parseFileToString(filePathCsv));
    }

    @Test
    public void testParseFileToStringFilePathHtml() {
        logger.info(parser.parseFileToString(filePathHtml));
    }

    @Test
    public void testParseFileToStringFilePathDoc() {
        logger.info(parser.parseFileToString(filePathDoc));
    }

}
