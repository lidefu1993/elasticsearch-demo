package com.bonc.es.web;

import com.bonc.es.config.EsDocumentOperator;
import com.bonc.es.entiry.param.CreateDocParam;
import com.bonc.es.entiry.param.UpdateDocParam;
import com.bonc.es.util.BaseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author lidefu
 * @date 2019/1/11 11:46
 */
@RestController
@Api(description = "ES document文档")
@RequestMapping("doc")
public class EsDocController {

    @ApiOperation(value = "创建文档")
    @PostMapping("/")
    public ResponseEntity createDoc(@RequestBody CreateDocParam param) throws IOException {
        return ResponseEntity.ok(EsDocumentOperator.createIndex(param.getIndexName(), BaseUtil.beanToMap(param.getPeopleInfo())));
    }

    @ApiOperation(value = "删除文档", notes = "indexName:索引名称 docId:文档Id")
    @DeleteMapping("/")
    public ResponseEntity delDoc(String indexName, String docId) throws IOException {
        return ResponseEntity.ok(EsDocumentOperator.delete(indexName, docId));
    }

    @ApiOperation(value = "修改文档", notes = "indexName:索引名称 docId:文档Id peopleInfo:文档信息")
    @PutMapping("/")
    public ResponseEntity updateDoc(@RequestBody UpdateDocParam param) throws IOException {
        return ResponseEntity.ok(EsDocumentOperator.update(param.getIndexName(), param.getDocId(), BaseUtil.beanToMap(param.getPeopleInfo())));
    }

}
