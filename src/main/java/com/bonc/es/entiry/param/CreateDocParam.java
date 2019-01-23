package com.bonc.es.entiry.param;

import com.bonc.es.entiry.PeopleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lidefu
 * @date 2019/1/11 11:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDocParam {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 人员信息
     */
    private PeopleInfo peopleInfo;

}
