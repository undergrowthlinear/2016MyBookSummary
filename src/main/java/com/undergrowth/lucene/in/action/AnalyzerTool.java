package com.undergrowth.lucene.in.action;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 显示词汇单元内容
 * @date 2017-02-13-21:13
 */
public class AnalyzerTool {

    private Logger logger = LoggerFactory.getLogger(AnalyzerTool.class);

    public void disTokenStreamInfo(String text, Analyzer analyzer) {
        try {
            TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
            logger.info("text:" + text + "\t" + "analyzer:" + analyzer.toString());
            while (tokenStream.incrementToken()) {
                // 碗模型
                CharTermAttribute cta = tokenStream.addAttribute(CharTermAttribute.class);
                OffsetAttribute oa = tokenStream.addAttribute(OffsetAttribute.class);
                PositionIncrementAttribute pia = tokenStream.addAttribute(PositionIncrementAttribute.class);
                TypeAttribute ta = tokenStream.addAttribute(TypeAttribute.class);
                logger.info(
                    pia.getPositionIncrement() + ":" + cta + "[" + oa.startOffset() + "-" + oa.endOffset()
                        + "]"
                        + ":" + ta.type());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        }
    }

}
