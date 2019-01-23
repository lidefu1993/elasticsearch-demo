import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.es.App;
import com.bonc.es.config.EsClient;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsResponse;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

/**
 * @author lidefu
 * @date 2019/1/18 10:16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CrossClusterTest {

    /**
     * 跨集群查询 首先设置集群
     */

    @Test
    public void updateSetting() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            ClusterUpdateSettingsRequest request = new ClusterUpdateSettingsRequest();
            Settings persistentSettings =
                    Settings.builder()
                            .put("cluster.remote.cluster_four.seeds", "172.16.36.142:9300")
                            .build();
            Map map = new HashMap();
            map.put("cluster.remote.cluster_four.seeds", Arrays.asList("172.16.36.142:9300"));
            request.persistentSettings(map);
            client.cluster().putSettings(request, RequestOptions.DEFAULT);
        }finally {
            client.close();
        }
    }

    @Test
    public void getSetting() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            ClusterGetSettingsRequest request = new ClusterGetSettingsRequest();
            ClusterGetSettingsResponse settings = client.cluster().getSettings(request, RequestOptions.DEFAULT);
            System.out.println(settings);
        }finally {
            client.close();
        }
    }

    @Test
    public void health(){

    }

    @Test
    public void crossClusterQuery() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchRequest searchRequest = new SearchRequest("cluster_one:people", "cluster_two:people");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("desc", "张飞"));
            searchRequest.source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(response);
        }finally {
            client.close();
        }
    }

    @Test
    public void crossClusterQueryByLowClient() throws IOException {
        RestClient client = EsClient.lowClient();
        try {
            Request request = new Request("GET", "/cluster_one:people,cluster_four:people/_search");
            String queryText = "{\n" +
                    "  \"query\": {\n" +
                    "    \"match\": {\n" +
                    "      \"desc\": \"张飞\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            HttpEntity entity = new NStringEntity(queryText, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            Response response = client.performRequest(request);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);
            Object responseObject = JSONObject.parse(responseString);
            System.out.println(responseString);
        }finally {
            client.close();
        }
    }

}
