package com.bonc.es.entiry.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lidefu
 * @date 2019/1/11 17:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchQueryParam extends QueryParam{

    /**
     * 字段名称
     */
    private String[] fieldNames;

}
