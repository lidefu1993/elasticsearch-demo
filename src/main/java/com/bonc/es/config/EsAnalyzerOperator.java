package com.bonc.es.config;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author lidefu
 * @date 2019/1/11 11:13
 */
public class EsAnalyzerOperator {

    /**
     * ik分词
     * @param text 语句
     * @return -
     */
    public static ActionResponse ikAnalyzer(String text) throws IOException {
        RestHighLevelClient client = EsClient.client();
        ActionResponse response;
        try {
            AnalyzeRequest request = analyzeRequest(text, "ik_max_word");
            response = client.indices().analyze(request, RequestOptions.DEFAULT);
        }finally {
            EsClient.close(client);
        }
        return response;
    }

    /**
     * 构建分析请求
     * @param text 文本
     * @param analyzer 分析器
     * @return -
     */
    public static AnalyzeRequest analyzeRequest(String text, String analyzer){
        AnalyzeRequest request = new AnalyzeRequest();
        request.text(text);
        request.analyzer(analyzer);
        return request;
    }


}
