import com.bonc.es.config.EsClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
/**
 * @author lidefu
 * @date 2019/1/18 17:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootTest.class)
public class EsScriptTest {

    /**
     * 数组追加
     * @throws IOException
     */
    @Test
    public void updateDocWithScript1() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            UpdateRequest request = new UpdateRequest("people", "people", "1xIPYGgBPDtjcCQKFBBG");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("newTag", "aaatest22");
            Script script = new Script(ScriptType.INLINE, "painless", "ctx._source.tags.add(params.newTag)", parameters);
            request.script(script);
            client.update(request, RequestOptions.DEFAULT);
        }finally {
            client.close();
        }
    }

    /**
     * 数字新增
     * @throws IOException
     */
    @Test
    public void updateDocWithScript2() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            UpdateRequest request = new UpdateRequest("people", "people", "1hKoXmgBPDtjcCQKlhB1");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("addAge", 1);
            Script script = new Script(ScriptType.INLINE, "painless", "ctx._source.age += params.addAge", parameters);
            request.script(script);
            client.update(request, RequestOptions.DEFAULT);
        }finally {
            client.close();
        }
    }

    /**
     * 查询使用script
     * @throws IOException
     */
    @Test
    public void  searchWithScript1() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            SearchRequest request = new SearchRequest("people");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchQuery("desc", "三国"));
            Map<String, Object> params = new HashMap<>();
            params.put("prefix", "吴国：");
            params.put("age", 2);
            params.put("name", "周瑜");
            Script script = new Script(ScriptType.INLINE, "painless", "doc['age'].value * params.age", params);
            sourceBuilder.scriptField("newAge", script);
            request.source(sourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            System.out.println(response);
        }finally {
            client.close();
        }
    }

}
