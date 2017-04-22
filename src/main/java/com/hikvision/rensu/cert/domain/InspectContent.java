package com.hikvision.rensu.cert.domain;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 行检项目索引
 * Created by rensu on 17/3/28.
 */
public class InspectContent {
    /**
     * 自增序号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 序号
     */
    private Long contendId;

    /**
     * 用例编号
     */
    private String caseId;

    /**
     * 用例名称
     */
    private String caseName;

    /**
     * 用例类型
     */
    private String caseType;


    /**
     * 用例说明
     */
    private String caseDescription;

}
