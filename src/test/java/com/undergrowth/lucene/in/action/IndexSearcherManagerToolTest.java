package com.undergrowth.lucene.in.action;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 近实时搜索的封装测试类
 * @date 2017-02-13-20:48
 */
public class IndexSearcherManagerToolTest {


  private String prefixPath = "E:\\code\\github\\2016MyBookSummary\\src\\test\\resources\\lucene\\51ctoLucene35\\indexsearchmanagertool\\";
  private String indexPath = prefixPath + "index";
  private String docPath = prefixPath + "docs";

  private IndexSearchManagerTool indexTool = null;

  @Before
  public void before() {
    indexTool = new IndexSearchManagerTool(indexPath);
  }

  @Test
  public void testIndex() {
    indexTool.getNrtManagerFileTool().index(indexPath, docPath);
    indexTool.getNrtManagerFileTool().commit();
  }

  @Test
  public void testDelete() {
    PhraseQuery query = new PhraseQuery();
    query.add(new Term("id", "87"));
    indexTool.getNrtManagerFileTool().delete(indexPath, query);
  }

  @Test
  public void testUpdate() {
    Term term = new Term("id", "66");
    indexTool.getNrtManagerFileTool().update(indexPath, term, docPath + "\\" + "con1.txt");
  }

  @Test
  public void testSearch() {
    for (int i = 0; i < 5; i++) {
      indexTool.getSearcherManagerFileTool().search(indexPath, "content", "Apache", 100);
      if (i == 0) {
        testUpdate();
      }
      if (i == 3) {
        testDelete();
      }
      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    indexTool.getNrtManagerFileTool().commit();
  }

  @Test
  public void testSearchQuery() {
    try {
      QueryParser parser = new QueryParser(Version.LUCENE_35, "content",
          indexTool.getSearcherManagerFileTool().getAnalyzer());
      Query query = parser.parse("Apache");
      indexTool.getSearcherManagerFileTool().search(indexPath, query, 100);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void testSearchQuerryPage() {
    try {
      QueryParser parser = new QueryParser(Version.LUCENE_35, "content",
          indexTool.getSearcherManagerFileTool().getAnalyzer());
      Query query = parser.parse("Apache");
      indexTool.getSearcherManagerFileTool().searchPage(indexPath, query, 1, 10);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
