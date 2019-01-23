package com.bonc.es.entiry.param;

import com.bonc.es.entiry.PeopleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lidefu
 * @date 2019/1/11 14:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDocParam {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 文档id
     */
    private String docId;

    /**
     * 人员信息
     */
    private PeopleInfo peopleInfo;

}
