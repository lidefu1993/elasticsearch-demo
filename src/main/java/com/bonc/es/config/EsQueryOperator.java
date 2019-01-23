package com.bonc.es.config;

import com.bonc.es.entiry.param.MatchQueryParam;
import com.bonc.es.entiry.param.QueryParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author lidefu
 * @date 2019/1/11 15:24
 */
public class EsQueryOperator {

    public static void main(String[] args) throws IOException {
    }

    private static final int FROM_DEFAULT = 0;

    /**
     * 检索结果集限制大小
     */
    private static final int MAX_LENGTH = 5000;

    /**
     * 全文检索
     * @param param 检索参数
     * @return -
     * @throws IOException -
     */
    public static SearchResponse matchQuery(MatchQueryParam param) throws IOException {
        RestHighLevelClient client = EsClient.client();
        SearchResponse response;
        try {
            response = client.search(searchRequest(param), RequestOptions.DEFAULT);
        }finally {
            EsClient.close(client);
        }
        return response;
    }

    private static SearchRequest searchRequest(MatchQueryParam param){
        SearchRequest request = new SearchRequest(param.getIndices());
        request.source(searchSourceBuilder(param));
        return request;
    }

    private static SearchSourceBuilder searchSourceBuilder(MatchQueryParam param){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        Arrays.stream(param.getFieldNames()).forEach(fieldName ->
                searchSourceBuilder.query(QueryBuilders.matchQuery(fieldName, param.getText())));
        //高亮
        if(param.getIsHighlight() != null && param.getIsHighlight()){
            highlight(searchSourceBuilder, param.getFieldNames());
        }
        //添加分页
        page(searchSourceBuilder, param.getFrom(), param.getSize());
        //排序
        sort(searchSourceBuilder, param.getSort());
        return searchSourceBuilder;
    }

    /**
     * 匹配内容高亮
     * @param searchSourceBuilder -
     * @param fields 查询字段
     */
    private static void highlight(SearchSourceBuilder searchSourceBuilder, String... fields){
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        Arrays.stream(fields).forEach(field -> highlightBuilder.field(new HighlightBuilder.Field(field)));
        searchSourceBuilder.highlighter(highlightBuilder);
    }

    /**
     * 构建分页
     * @param searchSourceBuilder -
     * @param from 开始索引位置 默认为0
     * @param size 结果集大小 最大5000
     */
    private static void page(SearchSourceBuilder searchSourceBuilder, Integer from, Integer size){
        from = from == null ? FROM_DEFAULT : from;
        size = ( size == null||size > MAX_LENGTH ) ? MAX_LENGTH : size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
    }

    /**
     * 添加排序
     * @param searchSourceBuilder -
     * @param sort 排序参数
     */
    private static void sort(SearchSourceBuilder searchSourceBuilder, QueryParam.Sort sort){
       if (sort == null){
           return;
       }
       searchSourceBuilder.sort(sort.getFiledName(), sort.order());
    }

    /**
     * 相关度排序
     * @param searchSourceBuilder -
     * @param order 排序方式
     */
    private static void scoreSort(SearchSourceBuilder searchSourceBuilder, SortOrder order){
        searchSourceBuilder.sort(new ScoreSortBuilder().order(order));
    }

}
