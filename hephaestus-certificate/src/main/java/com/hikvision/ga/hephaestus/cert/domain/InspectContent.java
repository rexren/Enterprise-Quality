package com.hikvision.ga.hephaestus.cert.domain;

import java.io.Serializable;

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
public class InspectContent implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

    /**
     * 自增序号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 外键：检测报告ID
     */
    @Column
    private Long inspectionId;
    
    /**
     * 用例编号/序号
     */
    @Column
    private String caseId;

    /**
     * 用例名称 /检测项目 /测试项目 /功能列表
     */
    @Column
    private String caseName;

    /**
     * 用例说明/技术标准/技术要求
     */
    @Column
    private String caseDescription;

    /**
     * 备注（选填）
     */
    @Column
    private String remarks;

	/**
     * 测试结果（选填）
     */
    @Column
    private String testResult;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getInspectionId() {
		return inspectionId;
	}

	public void setInspectionId(Long inspectionId) {
		this.inspectionId = inspectionId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
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

	public String getCaseDescription() {
		return caseDescription;
	}

	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

    public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public InspectContent(){
		
	}
}
