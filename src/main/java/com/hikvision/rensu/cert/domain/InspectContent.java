package com.hikvision.rensu.cert.domain;


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

    public InspectContent() {
    }

    public InspectContent(Long contendId, String caseId, String caseName, String caseType, String caseDescription) {
        this.contendId = contendId;
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

    public Long getContendId() {
        return contendId;
    }

    public void setContendId(Long contendId) {
        this.contendId = contendId;
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
