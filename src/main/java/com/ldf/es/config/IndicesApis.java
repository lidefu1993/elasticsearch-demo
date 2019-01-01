package com.ldf.es.config;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;

/**
 * @author ldf
 * @date 2019/1/1 11:18
 **/
public class IndicesApis {

    /**
     * 创建索引
     * @param indexName 索引名称
     * @param shards 分片数
     * @param replicas 副本数
     * @return 创建索引请求 实例
     */
    public static CreateIndexRequest createIndexRequest(String indexName, int shards, int replicas){
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", shards)
                .put("index.number_of_replicas", replicas));
        return request;
    }

    /**
     * 删除索引
     * @param index
     * @return
     */
    public static DeleteIndexRequest deleteIndexRequest(String index){
        return new DeleteIndexRequest(index);
    }

    /**
     * ES分析器
     * @param text 文本
     * @param analyzer 分析器名称
     * @return 分析器
     */
    public static AnalyzeRequest analyzeRequest(String text, String analyzer){
        AnalyzeRequest request = new AnalyzeRequest();
        request.text(text);
        request.analyzer(analyzer);
        return request;
    }

}
