import com.bonc.es.App;
import com.bonc.es.config.EsClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * aggregation 聚合统计
 * @author lidefu
 * @date 2019/1/29 9:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class AggregationQueryTest {

    /**
     * 取过滤结果集的分数的平均值
     * @throws IOException
     */
    @Test
    public void avg() throws IOException {
        try(RestHighLevelClient client = EsClient.client()) {
            SearchRequest request = new SearchRequest("stu");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchQuery("desc", "北京"));
            //平均分
            AvgAggregationBuilder avgScore = new AvgAggregationBuilder("avg_score");
            avgScore.field("score");
            sourceBuilder.aggregation(avgScore);
            request.source(sourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            System.out.println(response.getAggregations().toString());
        }
    }

    @Test
    public void complex() throws IOException {
        try(RestHighLevelClient client = EsClient.client()) {
            SearchRequest request = new SearchRequest("stu");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchQuery("desc", "北京"));
            //最大分数
            MaxAggregationBuilder maxScore = new MaxAggregationBuilder("maxScore");
            maxScore.field("score");
            sourceBuilder.aggregation(maxScore);
            //最小分数
            MinAggregationBuilder minScore = new MinAggregationBuilder("minScore");
            minScore.field("score");
            sourceBuilder.aggregation(minScore);
            request.source(sourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            Map<String, Aggregation> aggregationMap = response.getAggregations().asMap();
            ParsedMin parsedMin = (ParsedMin)aggregationMap.get("minScore");
            System.out.println("最小分数：" + parsedMin.getValue());
            ParsedMax parsedMax = (ParsedMax)aggregationMap.get("maxScore");
            System.out.println("最大分数：" + parsedMax.getValue());
            System.out.println(response.getAggregations().toString());
        }
    }

}
