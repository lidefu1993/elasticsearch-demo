package com.bonc.es.web;

import com.bonc.es.config.EsIndexOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author lidefu
 * @date 2019/1/11 11:31
 */
@RestController
@Api(description = "ES索引")
@RequestMapping("index")
public class EsIndexController {

    @ApiOperation(value = "创建索引", notes = "indexName:索引名称 shards:分片数 replicas:副本数")
    @PutMapping("/")
    public ResponseEntity createIndex(String indexName, int shards, int replicas) throws IOException {
        return ResponseEntity.ok(EsIndexOperator.createIndex(indexName, shards, replicas));
    }

    @ApiOperation(value = "删除索引", notes = "indexName:索引名称")
    @DeleteMapping("/")
    public ResponseEntity deleteIndex(String indexName) throws IOException {
        return ResponseEntity.ok(EsIndexOperator.deleteIndex(indexName));
    }

}
