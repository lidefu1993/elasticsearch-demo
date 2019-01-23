package com.bonc.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author lidefu
 * @date 2019/1/11 10:44
 */
public class EsClient {

    /**
     * Java High Level REST Client
     * @return Java High Level REST Client
     */
    public static RestHighLevelClient client(){
        return new RestHighLevelClient(
                RestClient.builder(
                        //172.16.74.112  172.16.36.148
//                        new HttpHost("172.16.74.112", 9200)
                        new HttpHost("172.16.36.148", 9200)
//                        ,new HttpHost("172.16.36.148", 9200)
                ));
    }

    /**
     * Java Low Level REST Client
     * @return Java Low Level REST Client
     */
    public static RestClient lowClient(){
        return RestClient.builder(
                new HttpHost("172.16.74.112", 9200, "http")
//                ,new HttpHost("172.16.36.200", 9200, "http")
        ).build();
    }



    public static void close(RestHighLevelClient client) throws IOException {
        if(client != null){
            client.close();
        }
    }


}
