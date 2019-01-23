import com.bonc.es.App;
import com.bonc.es.config.EsClient;
import com.bonc.es.config.EsDocumentOperator;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import java.io.IOException;

/**
 * @author lidefu
 * @date 2019/1/22 11:16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EsMappingsTest {

    /**
     * 添加字段
     * 根据fullText可以检索但是结果source中不存在该字段
     */
    @Test
    public void addFields() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            PutMappingRequest request = new PutMappingRequest("people");
            request.type("people");
            Map<String, Object> message = new HashMap<>();
            message.put("type", "text");
            message.put("copy_to", "fullText");
            Map<String, Object> properties = new HashMap<>();
            properties.put("detail3", message);
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("properties", properties);
            request.source(jsonMap);
            client.indices().putMapping(request, RequestOptions.DEFAULT);
        }finally {
            client.close();
        }
    }

    /**
     * 修改字段
     */
    @Test
    public void updateFields() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            PutMappingRequest request = new PutMappingRequest("people");
            request.type("people");
            Map<String, Object> message = new HashMap<>();
            message.put("type", "text");
            message.put("copy_to", "fullText");
            Map<String, Object> properties = new HashMap<>();
            properties.put("desc", message);
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("properties", properties);
            request.source(jsonMap);
            client.indices().putMapping(request, RequestOptions.DEFAULT);
        }finally {
            client.close();
        }
    }

    @Test
    public void updateDoc() throws IOException {
        RestHighLevelClient client = EsClient.client();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("phone", "15100000001");
            map.put("idCard", "wu-1");
            map.put("sex", "男");
            map.put("name", "孙权");
            map.put("birth", "1988-03-25");
            map.put("age", "31");
            map.put("desc", "孙权，字仲谋，吴郡富春人，东汉末三国时期吴的著名战略家、政治家、外交家，同时也是吴的缔造者及建国皇帝。而在孙权称帝之前，吴的群臣等对其称呼为将军或至尊");
            map.put("detail1", "孙权性格旷达开朗，仁爱明断，喜欢供养贤才，因此很早就与父兄齐名[45]。由于非常重视集体的力量[46]，能毫无保留地信任臣下[47][48]，甚至部下死后代为教养其孤儿[49]赡养其妻儿[50]及其父母[51]。也会调解部属纠纷[52][53]，亦下诏勿杀叛逃将领的妻子子女[54]。孙权与臣下的亲密关系也体现在称呼其表字上，甚至是对于初见的潘濬[55][56]，曾与陆逊当众对舞，又将自身所穿衣物皆赐之[57]。对于他国贤才，孙权也毫不掩饰地表达喜爱，如诸葛亮费祎邓芝宗预等[58][59][60]。孙盛因而称许孙权尽心关爱部下，令其甘心为自己拼命，是东吴能够立于江东的原因[61]。");
            map.put("detail2", "孙权天性活泼奔放，能言善辩，常常肆无忌惮地恶作剧、戏弄人[62]，经常开些无关紧要的玩笑[63][64]，即使是面对蜀汉来使也不例外[65]。其本人亦参与配合部下的戏谑[66][67]，吴国朝堂上下因此充满着快活的空气。");
            map.put("detail3", "孙权崇尚节俭，并效法大禹以卑宫为美，原本住的建业宫其实只是孙权早期的将军府而已，一直住到赤乌十年建材腐朽，还诏令将武昌宫拆了，把木材运来建业修缮，但其实当时武昌宫也有二十八年的历史不堪使用，这么做的目的是节省木料避免妨碍农桑工作[72]，由此也可知孙权对农业的重视。陆凯向孙皓劝谏时也称孙权时代“后宫列女，及诸织络，数不满百，米有畜积，货财有余”");
            EsDocumentOperator.update("people", "1RKnXmgBPDtjcCQK1BCa", map);
        }finally {
            client.close();
        }
    }
}
