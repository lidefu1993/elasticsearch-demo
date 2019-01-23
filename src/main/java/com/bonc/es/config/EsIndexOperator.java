package com.bonc.es.config;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;

/**
 * ES 索引操作
 * @author lidefu
 * @date 2019/1/11 10:58
 */
public class EsIndexOperator {

    /**
     * 创建索引
     * @param indexName 索引名称
     * @param shards 分片数
     * @param replicas 副本数
     */
    public static ActionResponse createIndex(String indexName, int shards, int replicas) throws IOException {
        RestHighLevelClient client = EsClient.client();
        ActionResponse response;
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.settings(Settings.builder()
                    .put("index.number_of_shards", shards)
                    .put("index.number_of_replicas", replicas));
            response = client.indices().create(request, RequestOptions.DEFAULT);
        }finally {
            client.close();
        }
        return response;
    }

    /**
     * 删除索引
     * @param indexName 索引名称
     * @return -
     */
    public static ActionResponse deleteIndex(String indexName) throws IOException {
        RestHighLevelClient client = EsClient.client();
        ActionResponse response;
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            response = client.indices().delete(request, RequestOptions.DEFAULT);
        }finally {
            client.close();
        }
        return response;
    }

}
