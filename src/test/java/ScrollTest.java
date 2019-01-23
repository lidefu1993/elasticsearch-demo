import com.bonc.es.App;
import com.bonc.es.config.EsClient;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * 游标查询
 * @author lidefu
 * @date 2019/1/21 14:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class ScrollTest {

    /**
     * ES分页查询获取后边页数的数据的时候，也会把前边的数据一起查出来 然后去掉
     * scroll游标查询的原理是第一次查询的时候，保存当时数据快照，下次查询携带scrollId从已经保存的数据里获取
     * 所以时间删除时间是必须的
     */

    /**
     * scroll第一次查询
     * @throws IOException
     */
    @Test
    public void scrollInit() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchRequest searchRequest = new SearchRequest("people");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("desc", "三国"));
            searchSourceBuilder.size(1);
            searchRequest.source(searchSourceBuilder);
            searchRequest.scroll(TimeValue.timeValueSeconds(120));
            Scroll scroll = new Scroll(TimeValue.MINUS_ONE);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHits hits = searchResponse.getHits();
            System.out.println(scrollId);
            System.out.println(hits);
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(30));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            System.out.println(searchScrollResponse.getScrollId());
            SearchHits hits1 = searchScrollResponse.getHits();
            System.out.println(hits1);
        }finally {
            client.close();
        }
    }

    @Test
    public void retrieve() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchScrollRequest scrollRequest = new SearchScrollRequest("DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAAAMMWWmo4Tmoxd2VSMXVHZDJjY3UzSFFaZw==");
            scrollRequest.scroll(TimeValue.timeValueSeconds(10));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            String scrollId = searchScrollResponse.getScrollId();
            SearchHits hits = searchScrollResponse.getHits();
            System.out.println(scrollId);
        }finally {
            client.close();
        }

    }

    @Test
    public void scrollQuery() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
            SearchRequest searchRequest = new SearchRequest("people");
            searchRequest.scroll(scroll);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("desc", "三国"));
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            while (searchHits != null && searchHits.length > 0) {
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();
            }
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();
            System.out.println(succeeded);
        }finally {
            client.close();
        }
    }

}
