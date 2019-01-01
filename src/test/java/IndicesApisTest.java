import com.alibaba.fastjson.JSON;
import com.ldf.es.App;
import com.ldf.es.config.EsClient;
import com.ldf.es.config.IndicesApis;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author ldf
 * @date 2019/1/1 11:19
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class IndicesApisTest {

    @Test
    public void createIndex() throws IOException {
        RestHighLevelClient client = EsClient.client();
        CreateIndexRequest createIndexRequest = IndicesApis.createIndexRequest("doc", 5, 2);
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        client.close();
    }

    @Test
    public void deleteIndex() throws IOException {
        RestHighLevelClient client = EsClient.client();
        DeleteIndexRequest request = IndicesApis.deleteIndexRequest("doc");
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        client.close();
    }

    @Test
    public void analyzer() throws IOException {
        RestHighLevelClient client = EsClient.client();
        AnalyzeRequest request = IndicesApis.analyzeRequest("东方国信", "ik_max_word");
        AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response.getTokens()));
        client.close();
    }

}
