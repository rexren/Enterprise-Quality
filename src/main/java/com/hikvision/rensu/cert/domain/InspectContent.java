package com.hikvision.rensu.cert.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    @ManyToOne
    @JoinColumn (name="inspectionId")
    private TypeInspection owner;
    
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
     * 测试分类（如可靠性测试、易用性测试、维护性测试等）
     */
    @Column
    private String catalog;
    
    /**
     * 测试结果（选填）
     */
    @Column
    private String testResult;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public TypeInspection getOwner() {
		return owner;
	}

	public void setOwner(TypeInspection owner) {
		this.owner = owner;
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

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	public InspectContent(Long id, TypeInspection owner, String caseId, String caseName, String caseDescription,
			String catalog, String testResult) {
		super();
		this.id = id;
		this.owner = owner;
		this.caseId = caseId;
		this.caseName = caseName;
		this.caseDescription = caseDescription;
		this.catalog = catalog;
		this.testResult = testResult;
	}

	public InspectContent(){
		
	}
}
