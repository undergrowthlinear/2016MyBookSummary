package com.undergrowth.lucene.in.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description Lucene的索引构建步骤 createIndex //1.创建Directory //2.创建IndexWriter //3.创建Document //4.为Document创建Field //5.使用IndexWriter将Document写入索引
 * //6.关闭InderWriter
 *
 * Lucene查询 //1.创建Directory //2.创建IndexReader //3.使用IndexReader创建IndexSearcher //4.创建Query //5.使用IndexSearcher搜索Query,返回TopDocs //6.显示TopDocs的ScoreDoc
 * //7.通过IndexReader/docId获取Document //8.通过Document的get获取字段 //9.关闭IndexReader
 * @date 2017-02-13-19:39
 */
public class LuceneIndexQuery {

    private Logger logger = LoggerFactory.getLogger(LuceneIndexQuery.class);

    public void index(String indexPath, String docPath) {
        IndexWriter indexWriter = null;
        try {
            // 1.创建Directory
            Directory directory = createDirectory(indexPath);
            // 2.创建IndexWriter
            indexWriter = createIndexWriter(directory);
            // 3.创建Document
            addDocument(docPath, indexWriter);
            // 6.关闭InderWriter

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        } finally {
            try {
                if (indexWriter != null) {
                    indexWriter.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error(e.toString());
            }
        }

    }

    public void search(String indexPath, String fieldName, String searchValue, int num) {
        IndexReader reader = null;
        try {
            // 1.创建Directory
            Directory directory = createDirectory(indexPath);
            // 2.创建IndexReader
            reader = IndexReader.open(directory);
            // 3.使用IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);
            // 4.创建Query
            Query query = createQuery(fieldName, searchValue);
            // 5.使用IndexSearcher搜索Query,返回TopDocs
            TopDocs topDocs = searcher.search(query, num);
            // 6.显示TopDocs的ScoreDoc
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            disSearchResult(indexPath, fieldName, searchValue, num, searcher, scoreDocs);
            // 9.关闭IndexReader

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        } finally {

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void search(String indexPath, Query query, int num) {
        IndexReader reader = null;
        try {
            // 1.创建Directory
            Directory directory = createDirectory(indexPath);
            // 2.创建IndexReader
            reader = IndexReader.open(directory);
            // 3.使用IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);
            // 4.创建Query
            // 5.使用IndexSearcher搜索Query,返回TopDocs
            TopDocs topDocs = searcher.search(query, num);
            // 6.显示TopDocs的ScoreDoc
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            disSearchResult(indexPath, query.toString(), null, num, searcher, scoreDocs);
            // 9.关闭IndexReader

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void disSearchResult(String indexPath, String fieldName, String searchValue, int num,
        IndexSearcher searcher, ScoreDoc[] scoreDocs) throws IOException {
        logger.info(
            "indexPath:" + indexPath + "\tfieldName:" + fieldName + "\tsearchValue:" + searchValue
                + "\tnum:"
                + num);
        for (int i = 0; i < scoreDocs.length; i++) {
            // 7.通过IndexReader/docId获取Document
            Document document = searcher.doc(scoreDocs[i].doc);
            // 8.通过Document的get获取字段
            logger.info("name:" + document.get("name") + "\tpath:" + document.get("path") + "\tsize:"
                + document.get("size") + "\tcontent:" + document.get("content"));
        }
    }

    private Query createQuery(String fieldName, String searchValue) throws ParseException {
        Analyzer a = createAnalyzer();
        QueryParser parser = new QueryParser(Version.LUCENE_35, fieldName, a);
        Query query = parser.parse(searchValue);
        logger.info(query.toString());
        return query;
    }

    private Directory createDirectory(String indexPath) throws IOException {
        Directory directory = FSDirectory.open(new File(indexPath));
        return directory;
    }

    private void addDocument(String docPath, IndexWriter indexWriter)
        throws FileNotFoundException, IOException {
        File files = new File(docPath);
        if (files.isDirectory()) {
            for (File file : files.listFiles()) {
                writeDocument(indexWriter, file);
            }
        } else {
            writeDocument(indexWriter, files);
        }
    }

    private void writeDocument(IndexWriter indexWriter, File file)
        throws FileNotFoundException, IOException {
        // 4.为Document创建Field
        Document document = addDocumentField(file);
        // 5.使用IndexWriter将Document写入索引
        indexWriter.addDocument(document);
    }

    private IndexWriter createIndexWriter(Directory directory) throws IOException {
        Analyzer analyzer = createAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter indexWriter = new IndexWriter(directory, conf);
        return indexWriter;
    }

    public Analyzer createAnalyzer() {
        //Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
        Analyzer analyzer = new IKAnalyzer(true);
        return analyzer;
    }

    private Document addDocumentField(File file) throws FileNotFoundException {
        Document document = new Document();
        // indexed tokenized stored
        document.add(new Field("content", new FileReader(file)));
        document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(
            new Field("size", String.valueOf(file.length()), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("name", file.getName(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("name_size", file.getName() + " " + String.valueOf(file.length()),
            Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new NumericField("sizeInt").setLongValue(file.length()));
        logger.info("add document:" + document);
        return document;
    }
}
