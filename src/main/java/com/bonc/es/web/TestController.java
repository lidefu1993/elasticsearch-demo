package com.bonc.es.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lidefu
 * @date 2019/1/11 10:29
 */
@RestController
@RequestMapping("test")
public class TestController {

    @ApiOperation("测试1")
    @GetMapping("/")
    public String test(){
        return "success!";
    }

}
