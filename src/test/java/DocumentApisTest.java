import com.ldf.es.App;
import com.ldf.es.config.DocumentApis;
import com.ldf.es.config.EsClient;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ldf
 * @date 2019/1/1 13:50
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class DocumentApisTest {

    @Test
    public void indexRequest() throws IOException {
        RestHighLevelClient client = EsClient.client();
        Map<String, Object> source = new HashMap<>();
        source.put("name", "关羽"); source.put("age", 24); source.put("birth", "1995-12-21"); source.put("sex", "男");
        source.put("desc", "武器：青龙偃月刀；外号：关二爷；事迹：刮骨疗伤，斩颜良 诛文丑, 温酒斩华雄");
        IndexRequest request = DocumentApis.indexRequest("doc", source);
        client.index(request, RequestOptions.DEFAULT);
        client.close();
    }

    @Test
    public void deleteRequest() throws IOException {
        RestHighLevelClient client = EsClient.client();
        DeleteRequest request = DocumentApis.deleteRequest("doc", "qwsACGgBWzwfzQAYTtnU");
        client.delete(request, RequestOptions.DEFAULT);
        client.close();
    }

}
