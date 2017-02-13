package com.undergrowth.lucene.in.action;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 近实时索引
 * @date 2017-02-13-20:39
 */
public class LuceneNRTManager {

    private Logger logger = LoggerFactory.getLogger(LuceneNRTManager.class);
    private String[] data = { "name", "path", "content", "size", "lastTime", "length", "id" };
    private static IndexWriter indexWriter = null;
    private NRTManager nrtManagert = null;
    private SearcherManager searcherManager = null;
    private HashSet<Integer> randomNumSet = new HashSet<>();
    private Random randomNum = new Random();

    public LuceneNRTManager(String indexPath) {
        try {
            nrtManagert = new NRTManager(getIndexWriter(getDirectory(indexPath)), new SearcherWarmer() {

                @Override
                public void warm(IndexSearcher s) throws IOException {
                    // TODO Auto-generated method stub
                    logger.info("reopen");
                }
            });
            searcherManager = nrtManagert.getSearcherManager(true);
            NRTManagerReopenThread nrtManagerReopenThread = new NRTManagerReopenThread(nrtManagert, 5.0, 0.025);
            nrtManagerReopenThread.setDaemon(true);
            nrtManagerReopenThread.setName("openThread");
            nrtManagerReopenThread.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        }
    }

    public void index(String indexPath, String docPath) {
        try {
            // 3.创建Document
            writeDocument(new File(docPath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        }

    }

    public void delete(String indexPath, Query query) {
        try {
            logger.info("query:" + query);
            nrtManagert.deleteDocuments(query);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        }
    }

    public void update(String indexPath, Term term, String updateFileNamePath) {
        try {
            nrtManagert.updateDocument(term, addDocumentField(new File(updateFileNamePath)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        }
    }

    private Directory getDirectory(String indexPath) throws IOException {
        Directory directory = FSDirectory.open(new File(indexPath));
        return directory;
    }

    private void writeDocument(File file) throws FileNotFoundException, IOException {
        // 4.为Document创建Field
        nrtManagert.deleteAll();
        for (File indexFile : file.listFiles()) {
            Document document = addDocumentField(indexFile);
            // 5.使用IndexWriter将Document写入索引
            nrtManagert.addDocument(document);
        }
    }

    /**
     * 单例保存indexWriter
     *
     * @param directory
     * @return indexWriter
     * @throws IOException
     */
    private IndexWriter getIndexWriter(Directory directory) throws IOException {
        if (indexWriter == null) {
            synchronized (LuceneNRTManager.class) {
                if (indexWriter == null) {
                    Analyzer analyzer = createAnalyzer();
                    IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, analyzer);
                    indexWriter = new IndexWriter(directory, conf);
                }
            }
        }
        return indexWriter;
    }

    private Analyzer createAnalyzer() {
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
        return analyzer;
    }

    public Analyzer getAnalyzer() {
        return createAnalyzer();
    }

    private Document addDocumentField(File file) throws FileNotFoundException {
        Document document = new Document();
        // indexed tokenized stored
        // "name","path","content","size","lastTime"
        document.add(new Field(data[0], file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field(data[1], file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field(data[2], new FileReader(file)));
        document.add(new NumericField(data[3], Field.Store.YES, true).setIntValue((int) (file.length())));
        document.add(new NumericField(data[4], Field.Store.YES, true).setLongValue(file.lastModified()));
        document.add(new Field(data[5], String.valueOf(file.length()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field(data[6], String.valueOf(getRandm()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        logger.info("add document:" + document);
        return document;
    }

    private int getRandm() {
        int ran = randomNum.nextInt(1000);
        while (!randomNumSet.add(ran)) {
            ran = randomNum.nextInt(1000);
        }
        ;
        return ran;
    }

    public SearcherManager getSearcherManager() {
        return searcherManager;
    }

    public void commit() {
        try {
            if (indexWriter != null)
                indexWriter.commit();
        } catch (CorruptIndexException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.toString());
        }
    }
}
