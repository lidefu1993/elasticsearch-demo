package com.bonc.es.entiry.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.sort.SortOrder;


/**
 * @author lidefu
 * @date 2019/1/11 17:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryParam {

    /**
     * 索引集合
     */
    private String[] indices;

    /**
     * 检索内容
     */
    private String text;

    /**
     * 是否高亮
     */
    private Boolean isHighlight;

    /**
     * 结果集开始索引位置
     */
    Integer from;

    /**
     * 结果集大小
     */
    Integer size;

    /**
     * 排序方式
     */
    Sort sort;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sort{

        /**
         * 排序字段
         */
        private String filedName;

        /**
         * 排序方式 正序、倒序
         * null 和 true 正序  false倒序
         */
        private Boolean order;

        public SortOrder order(){
            if(order == null || order){
                return SortOrder.ASC;
            }
            return SortOrder.DESC;
        }

    }
}
