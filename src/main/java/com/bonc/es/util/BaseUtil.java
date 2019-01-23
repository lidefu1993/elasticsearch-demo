package com.bonc.es.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
/**
 * @author lidefu
 * @date 2019/1/11 11:54
 */
public class BaseUtil {

    /**
     * bean 转 map
     * @param t bean
     * @param <T> -
     * @return map
     */
    public static<T> Map beanToMap(T t){
        return JSONObject.parseObject(JSON.toJSONString(t), Map.class);
    }

}
