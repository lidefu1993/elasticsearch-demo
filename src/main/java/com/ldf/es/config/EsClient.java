package com.ldf.es.config;


import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

/**
 * @author ldf
 * @date 2019/1/1 10:08
 **/
public class EsClient {

    public static void main(String[] args) {

    }

    /**
     * RestHighLevelClient
     * @return RestHighLevelClient 实例
     */
    public static RestHighLevelClient client(){
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("172.16.36.148", 9200, "http")
                ));
    }


}
