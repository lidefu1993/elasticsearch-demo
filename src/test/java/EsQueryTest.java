import com.bonc.es.App;
import com.bonc.es.config.EsClient;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author lidefu
 * @date 2019/1/16 9:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EsQueryTest {


    public static void main(String[] args) throws IOException {
    }

    @Test
    public void matchQuery() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchRequest request = new SearchRequest("people", "people-2");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchQuery("detail3", "能言善辩"));
            request.source(sourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            System.out.println(response.toString());
        }finally {
            client.close();
        }
    }

    /**
     * 复杂组合查询
     */
    @Test
    public void complexQuery() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchRequest request = new SearchRequest("people");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//            boolQuery.mustNot(QueryBuilders.matchQuery("name", "孙尚香"));
//            boolQuery.must(QueryBuilders.matchQuery("desc", "孙权"));
//            boolQuery.should(QueryBuilders.matchQuery("desc", "三国"));
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
            rangeQuery.gte(33).lte(40).boost(0.2f);
            boolQuery.should(rangeQuery);
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("desc", "孙权");
            matchQuery.boost(0.8f);
            boolQuery.should(matchQuery);
            sourceBuilder.query(boolQuery);
            request.source(sourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            System.out.println(1);
        }finally {
            client.close();
        }
    }

    /**
     * 评分函数查询
     */
    @Test
    public void functionScoreQuery() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchRequest request = new SearchRequest("people");
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("name", "张飞"),
                            ScoreFunctionBuilders.weightFactorFunction(0.9f)),
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("desc", "张飞"),
                            ScoreFunctionBuilders.weightFactorFunction(0.1f)
                    )
            };
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.functionScoreQuery(QueryBuilders.multiMatchQuery("张飞", "name", "desc"), functions));
            request.source(searchSourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            System.out.println(response);
        }finally {
            client.close();
        }
    }

    /**
     * 总相关度评分减去负面影响的评分
     */
    @Test
    public void boostingQuery() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchRequest request = new SearchRequest("people");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder positiveQuery = QueryBuilders.matchQuery("fullText", "能言善辩");
            QueryBuilder negativeQuery = QueryBuilders.matchQuery("detail2", "能言善辩");
            searchSourceBuilder.query(QueryBuilders.boostingQuery(positiveQuery, negativeQuery).negativeBoost(0.33f));
            request.source(searchSourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            System.out.println(response);
        }finally {
            client.close();
        }
    }

    /**
     * 非检索字段高亮
     */
    @Test
    public void highlight() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchRequest searchRequest = new SearchRequest("people");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("name", "孙尚香"));
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.requireFieldMatch(false);
            highlightBuilder.field("*");
            searchSourceBuilder.highlighter(highlightBuilder);
            searchRequest.source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            Arrays.stream(hits).forEach(hit -> {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                System.out.println(sourceAsMap);
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                System.out.println(highlightFields);
            });
        }finally {
            client.close();
        }
    }

    /**
     {
     "query": {
     "boosting": {
     "positive": {
     "match": {
     "desc": "关羽"
     }
     },
     "negative": {
     "match": {
     "desc": "曹仁"
     }
     },
     "negative_boost": 0.2
     }
     }
     }
     * @throws IOException
     */
    @Test
    public void boostingQueryByLowClient() throws IOException {
        RestClient client = EsClient.lowClient();
        try {
            String text = "{\n" +
                    "    \"query\": {\n" +
                    "        \"boosting\" : {\n" +
                    "            \"positive\" : {\n" +
                    "                \"match\" : {\n" +
                    "                    \"desc\" : \"周瑜\"\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"negative\" : {\n" +
                    "                 \"match\" : {\n" +
                    "                     \"desc\" : \"孙权\"\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"negative_boost\" : 0.2\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            Request request = new Request("GET", "/people/_search");
            Response response = client.performRequest(request);
            HttpEntity entity = new NStringEntity(text, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            System.out.println(EntityUtils.toString(response.getEntity()));
        }finally {
            client.close();
        }
    }

    @Test
    public void lowClientQueryTest() throws IOException {
        RestClient client = EsClient.lowClient();
        try {
            String text = "{\n" +
                    "  \"query\": {\n" +
                    "    \"match\": {\n" +
                    "      \"name\": \"孙尚香\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            Request request = new Request("GET", "/people/_search");
            HttpEntity entity = new NStringEntity(text, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            Response response = client.performRequest(request);
            System.out.println(EntityUtils.toString(response.getEntity()));
        }finally {
            client.close();
        }
    }
}
