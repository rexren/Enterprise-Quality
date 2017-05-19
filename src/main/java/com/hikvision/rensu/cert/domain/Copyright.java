package com.hikvision.rensu.cert.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Copyright implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5436058156917876411L;
	
	/**
	 * 自增ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
     * 软件名称
     */
    @Column(nullable= false)
    private String softwareName;
    
	/**
     * 软件简称
     */
    @Column
    private String abbreviation;
	
	/**
     * 软件著作权登记号
     */
    @Column
    private String crNo;

	/**
     * 软件著作权签发日期
     */
    @Column
    private Date crDate;
	
	/**
     * 软件著作权文件链接
     */
    @Column
    private String crUrl;

	/**
     * 软件著作权认证/测试机构
     */
    @Column
    private String crOrganization;
	
	/**
     * 软件著作权-软件类型
     */
    @Column
    private String crSoftwareType;

	/**
     * 软件产品登记证书号
     */
    @Column
    private String rgNo;

	/**
     * 软件产品登记证书登记日期
     */
    @Column
    private Date rgDate;
	
	/**
     * 软件产品登记证书登记有效期（至）
     */
    @Column
    private Date rgExpiryDate;
	
	/**
     * 软件产品登记证书文件链接
     */
    @Column
    private String rgUrl;

	/**
     * 软件产品登记证认证/测试机构
     */
    @Column
    private String rgOrganization;

    /**
     * 软件评测报告
     */
    @Column
    private String epNo;
	
	/**
     * 软件评测报告签发日期
     */
    @Column
    private Date epDate;
	
	/**
     * 软件评测报告链接
     */
    @Column
    private String epUrl;

	/**
     * 软件评测报告认证/测试机构
     */
    @Column
    private String epOrganization;

    
    /**
     * 类别界定报告编号
     */
    @Column
    private String cdNo;
	
	/**
     * 类别界定报告界定日期
     */
    @Column
    private Date cdDate;
	
	/**
     * 类别界定报告链接
     */
    @Column
    private String cdUrl;

	/**
     * 类别界定报告认证/测试机构
     */
    @Column
    private String cdOrganization;

	/**
     * 软件型号
     */
    @Column
    private String model;

	/**
     * 负责人
     */
    @Column
    private String charge;
    
    /**
     * 创建时间
     */
    @Column
    private Date createDate;


    /**
     * 更新时间
     */
    @Column
    private Date updateDate;
    
    /**
     * 更新人员
     */
    @Column
    private String operator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getCrNo() {
		return crNo;
	}

	public void setCrNo(String crNo) {
		this.crNo = crNo;
	}

	public Date getCrDate() {
		return crDate;
	}

	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}

	public String getCrUrl() {
		return crUrl;
	}

	public void setCrUrl(String crUrl) {
		this.crUrl = crUrl;
	}

	public String getCrOrganization() {
		return crOrganization;
	}

	public void setCrOrganization(String crOrganization) {
		this.crOrganization = crOrganization;
	}

	public String getCrSoftwareType() {
		return crSoftwareType;
	}

	public void setCrSoftwareType(String crSoftwareType) {
		this.crSoftwareType = crSoftwareType;
	}

	public String getRgNo() {
		return rgNo;
	}

	public void setRgNo(String rgNo) {
		this.rgNo = rgNo;
	}

	public Date getRgDate() {
		return rgDate;
	}

	public void setRgDate(Date rgDate) {
		this.rgDate = rgDate;
	}

	public Date getRgExpiryDate() {
		return rgExpiryDate;
	}

	public void setRgExpiryDate(Date rgExpiryDate) {
		this.rgExpiryDate = rgExpiryDate;
	}

	public String getRgUrl() {
		return rgUrl;
	}

	public void setRgUrl(String rgUrl) {
		this.rgUrl = rgUrl;
	}

	public String getRgOrganization() {
		return rgOrganization;
	}

	public void setRgOrganization(String rgOrganization) {
		this.rgOrganization = rgOrganization;
	}

	public String getEpNo() {
		return epNo;
	}

	public void setEpNo(String epNo) {
		this.epNo = epNo;
	}

	public Date getEpDate() {
		return epDate;
	}

	public void setEpDate(Date epDate) {
		this.epDate = epDate;
	}

	public String getEpUrl() {
		return epUrl;
	}

	public void setEpUrl(String epUrl) {
		this.epUrl = epUrl;
	}

	public String getEpOrganization() {
		return epOrganization;
	}

	public void setEpOrganization(String epOrganization) {
		this.epOrganization = epOrganization;
	}

	public String getCdNo() {
		return cdNo;
	}

	public void setCdNo(String cdNo) {
		this.cdNo = cdNo;
	}

	public Date getCdDate() {
		return cdDate;
	}

	public void setCdDate(Date cdDate) {
		this.cdDate = cdDate;
	}

	public String getCdUrl() {
		return cdUrl;
	}

	public void setCdUrl(String cdUrl) {
		this.cdUrl = cdUrl;
	}

	public String getCdOrganization() {
		return cdOrganization;
	}

	public void setCdOrganization(String cdOrganization) {
		this.cdOrganization = cdOrganization;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    

}