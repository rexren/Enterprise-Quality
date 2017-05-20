package com.hikvision.rensu.cert.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 行检项目索引
 * Created by rensu on 17/3/28.
 */
@Entity
public class InspectContent {
    /**
     * 自增序号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用例编号/序号
     */
    @Column
    private String caseId;

    /**
     * 用例名称
     */
    @Column
    private String caseName;

    /**
     * 用例类型
     */
    @Column
    private String caseType;


    /**
     * 用例说明
     */
    @Column
    private String caseDescription;

    public InspectContent() {
    }

    public InspectContent( String caseId, String caseName, String caseType, String caseDescription) {
        this.caseId = caseId;
        this.caseName = caseName;
        this.caseType = caseType;
        this.caseDescription = caseDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }
}
