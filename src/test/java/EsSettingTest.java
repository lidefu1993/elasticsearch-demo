import com.bonc.es.App;
import com.bonc.es.config.EsClient;
import com.bonc.es.config.EsDocumentOperator;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

/**
 * @author lidefu
 * @date 2019/1/16 14:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EsSettingTest {

    /**
     * 设置默认分词器为IK
     */
    @Test
    public void defaultIk() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            UpdateSettingsRequest request = new UpdateSettingsRequest("people");
            Map<String, Object> params = new HashMap<>();
            params.put("index.analysis.analyzer.default.type", "ik_max_word");
            request.settings(params);
            client.indices().putSettings(request, RequestOptions.DEFAULT);
        }finally {
            client.close();
        }
    }

    @Test
    public void addDoc() throws IOException {
        RestHighLevelClient client = EsClient.client();
        Map map = new HashMap();
        map.put("name", "刘亦菲");
        map.put("desc", "神雕侠侣");
        ActionResponse response = EsDocumentOperator.createIndex("test", map);
        client.close();
    }

    @Test
    public void updateDoc() throws IOException {
        RestHighLevelClient client = EsClient.client();
        Map map = new HashMap();
        map.put("name", "刘亦菲");
        map.put("desc", "神雕侠侣");
        EsDocumentOperator.update("test", "jqjYVWgBJsDZjdFr-kY8", map);
        client.close();
    }

    @Test
    public void enableSource() throws IOException {
        RestHighLevelClient client = EsClient.client();
        PutMappingRequest request = new PutMappingRequest("test");
        request.type("test");
        request.source("{\n" +
                "      \"_source\": {\n" +
                "        \"enabled\": false\n" +
                "      }\n" +
                "    }", XContentType.JSON);
        AcknowledgedResponse response = client.indices().putMapping(request, RequestOptions.DEFAULT);
        System.out.println(response);
        client.close();
    }

    @Test
    public void fieldStore() throws IOException {
        RestHighLevelClient client = EsClient.client();
        PutMappingRequest request = new PutMappingRequest("test");
        request.type("test");
        request.source("{\n" +
                "  \"properties\": {\n" +
                "\t\"name\": {\n" +
                "\t  \"type\": \"text\",\n" +
                "\t  \"store\": true \n" +
                "\t},\n" +
                "\t\"desc\": {\n" +
                "\t  \"type\": \"text\",\n" +
                "\t  \"store\": true \n" +
                "\t}\n" +
                "  }\n" +
                "}", XContentType.JSON);
        AcknowledgedResponse response = client.indices().putMapping(request, RequestOptions.DEFAULT);
        System.out.println(response);
        client.close();
    }


}
