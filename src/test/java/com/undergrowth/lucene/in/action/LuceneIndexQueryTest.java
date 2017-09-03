package com.undergrowth.lucene.in.action;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description Lucene索引_搜索简单测试
 * @date 2017-02-13-19:51
 */
public class LuceneIndexQueryTest {

  LuceneIndexQuery bool = null;
  private String prefixPath = "E:\\code\\github\\2016MyBookSummary\\src\\test\\resources\\lucene\\51ctoLucene35\\bool\\";
  private String indexPath = prefixPath + "index";
  private String docPath = prefixPath + "docs";

  @Before
  public void setUp() throws Exception {
    bool = new LuceneIndexQuery();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testIndex() {
    bool.index(indexPath, docPath);
  }

  @Test
  public void testBooleanQuerySearch() {
    TermQuery termQuery1 = new TermQuery(new Term("name", "con2.txt"));
    TermQuery termQuery2 = new TermQuery(new Term("content", "房间"));
    BooleanQuery query = new BooleanQuery();
    //或
    query.add(termQuery1, BooleanClause.Occur.SHOULD);
    query.add(termQuery2, BooleanClause.Occur.MUST);
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testBooleanQueryTokenSearch() {
    TermQuery termQuery1 = new TermQuery(new Term("path", "51ctoLucene35"));
    TermQuery termQuery2 = new TermQuery(new Term("content", "房间"));
    BooleanQuery query = new BooleanQuery();
    //必须
    query.add(termQuery1, BooleanClause.Occur.MUST);
    query.add(termQuery2, BooleanClause.Occur.MUST);
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testQueryParserSearch() throws ParseException {
    QueryParser queryParser = new QueryParser(Version.LUCENE_35, "content", bool.createAnalyzer());
    Query query = queryParser.parse("总统");
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testTermRangeQuerySearch() {
    TermRangeQuery query = new TermRangeQuery("size", "100", "500", true, true);
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testNumericRangeQuerySearch() {
    Query query = NumericRangeQuery.newIntRange("sizeInt", 100, 500, true, true);
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testPhraseQuerySearch() {
    PhraseQuery query = new PhraseQuery();
    query.setSlop(3);
    query.add(new Term("content", "饭店"));
    query.add(new Term("content", "房间"));
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testMultiPhraseQuerySearch() {
    MultiPhraseQuery query = new MultiPhraseQuery();
    query.setSlop(3);
    query.add(new Term("content", "饭店"));
    query.add(new Term("content", "房间"));
    query.add(new Term[]{new Term("content", "侍者")});
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testFuzzyQuerySearch() {
    FuzzyQuery query = new FuzzyQuery(new Term("content", "饭店"), 0.3f, 2);
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testWildcardQuerySearch() {
    WildcardQuery query = new WildcardQuery(new Term("content", "饭店*"));
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testRegexpQuerySearch() {
    //RegexpQuery query=new RegexpQuery(new Term("content","饭店*"));
    Query query = null;
    bool.search(indexPath, query, 10);
  }

  @Test
  public void testPrefixQuerySearch() {
    PrefixQuery query = new PrefixQuery(new Term("content", "饭店"));
    bool.search(indexPath, query, 10);
  }

}
