package com.ldf.es.config;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import java.util.Map;

/**
 * @author ldf
 * @date 2019/1/1 13:46
 **/
public class DocumentApis {

    private static final String DEFAULT_TYPE = "doc_type";
    /**
     * 创建文档索引
     * @param index 索引
     * @param source 资源
     * @return IndexRequest
     */
    public static IndexRequest indexRequest(String index, Map source){
        IndexRequest request = new IndexRequest(index, DEFAULT_TYPE);
        request.source(source);
        return request;
    }

    /**
     * 删除文档
     * @param index
     * @param id
     * @return
     */
    public static DeleteRequest deleteRequest(String index, String id){
        return new DeleteRequest(index, DEFAULT_TYPE, id);
    }

    

}
