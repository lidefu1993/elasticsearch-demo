package com.bonc.es.config;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Map;
/**
 * @author lidefu
 * @date 2019/1/11 11:19
 */
public class EsDocumentOperator {

    /**
     * 创建文档索引
     * @param index 索引
     * @param source 文档数据
     * @return
     * @throws IOException
     */
    public static ActionResponse createIndex(String index, Map source) throws IOException {
        RestHighLevelClient client = EsClient.client();
        ActionResponse response;
        try {
            //type与index保持一致
            IndexRequest request = new IndexRequest(index, index);
            request.source(source);
            response = client.index(request, RequestOptions.DEFAULT);
        }finally {
            EsClient.close(client);
        }
        return response;
    }

    /**
     * 删除文档记录
     * @param index 索引
     * @param docId 文档id
     * @return -
     */
    public static ActionResponse delete(String index, String docId) throws IOException {
        RestHighLevelClient client = EsClient.client();
        ActionResponse response;
        try {
            DeleteRequest request = new DeleteRequest(index, index, docId);
            response = client.delete(request, RequestOptions.DEFAULT);
        }finally {
            EsClient.close(client);
        }
        return response;
    }

    /**
     * 更新文档信息
     * @param index 索引名称
     * @param docId 文档id
     * @param source 文档内容
     * @return -
     * @throws IOException -
     */
    public static ActionResponse update(String index, String docId, Map source) throws IOException {
        ActionResponse response;
        RestHighLevelClient client = EsClient.client();
        try {
            UpdateRequest request = new UpdateRequest(index, index, docId);
            request.doc(source);
            response = client.update(request, RequestOptions.DEFAULT);
        }finally {
            EsClient.close(client);
        }
        return response;
    }


}
