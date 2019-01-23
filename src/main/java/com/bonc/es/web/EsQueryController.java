package com.bonc.es.web;

import com.bonc.es.config.EsQueryOperator;
import com.bonc.es.entiry.param.MatchQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author lidefu
 * @date 2019/1/11 17:31
 */
@RestController
@Api(description = "ES 检索")
@RequestMapping("query")
public class EsQueryController {

    @ApiOperation(value = "matchQuery 单字段全文检索；若需高亮显示要将结果的内容替换一下")
    @PostMapping("matchQuery")
    public ResponseEntity matchQuery(@RequestBody MatchQueryParam param) throws IOException {
        SearchResponse response = EsQueryOperator.matchQuery(param);
        return ResponseEntity.ok(response.getHits());
    }

}
