package com.bonc.es.web;

import com.bonc.es.config.EsAnalyzerOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author lidefu
 * @date 2019/1/11 11:42
 */
@RestController
@Api(description = "ES 分析器")
@RequestMapping("analyzer")
public class EsAnalyzerController {

    @ApiOperation(value = "ik分词器", notes = "text:文本")
    @GetMapping("ik")
    public ResponseEntity ikAnalyzer(String text) throws IOException {
        return ResponseEntity.ok(EsAnalyzerOperator.ikAnalyzer(text));
    }

}
