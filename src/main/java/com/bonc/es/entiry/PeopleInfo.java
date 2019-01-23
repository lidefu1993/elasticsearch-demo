package com.bonc.es.entiry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 人员信息实体类
 * @author lidefu
 * @date 2019/1/11 11:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleInfo {

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 电话号
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private int age;

    /**
     * 生日
     */
    private String birth;

    /**
     * 描述
     */
    private String desc;

    private List<String> history;

}
