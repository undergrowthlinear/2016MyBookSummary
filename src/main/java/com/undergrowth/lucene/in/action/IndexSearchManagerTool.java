package com.undergrowth.lucene.in.action;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 近实时搜索的封装类
 * @date 2017-02-13-20:45
 */
public class IndexSearchManagerTool {

    private LuceneNRTManager nrtManagerFileTool;
    private LuceneSearcherManager searcherManagerFileTool;

    public IndexSearchManagerTool(String indexPath) {
        nrtManagerFileTool = new LuceneNRTManager(indexPath);
        searcherManagerFileTool = new LuceneSearcherManager(nrtManagerFileTool.getSearcherManager());
    }

    public LuceneNRTManager getNrtManagerFileTool() {
        return nrtManagerFileTool;
    }

    public LuceneSearcherManager getSearcherManagerFileTool() {
        return searcherManagerFileTool;
    }

}
