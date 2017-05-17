package com.hikvision.rensu.cert.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * CCC认证
 * Created by SherryLang on 25/5/25.
 */
@Entity
public class CccPage implements Serializable{

	/**
	 * 
	 */
	private static long serialVersionUID = -1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**	
	 * 文件编号  
	 */
	@Column (nullable = false)
	private String docNo;
	
	/**
     * 产品型号
     */
    @Column(nullable = false)
    private String model;
    
    /**
     * 产品名称
     */
    @Column(nullable = false)
    private String productName;

    /**
     * 颁发日期
     */
    @Column(nullable = false)
    private Date awardDate;

    /**
     * 有效日期
     */
    @Column(nullable = false)
    private Date expiryDate;
    
    /**
     * 证书系统链接
     */
    @Column
    private String url;

    /**
     * 认证机构
     */
    @Column(nullable = false)
    private String organization;
    
	/**
     * 备注
     */
    @Column
    private String remarks;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getAwardDate() {
		return awardDate;
	}

	public void setAwardDate(Date awardDate) {
		this.awardDate = awardDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	@Override
	public String toString() {
		return "CCCPage [id=" + id + 
				", docNo=" + docNo + 
				", model=" + model + 
				", productName=" + productName + 
				", awardDate=" + awardDate + 
				", expiryDate=" + expiryDate + 
				", url=" + url + 
				", organization="+ organization + 
				", remarks=" + remarks + 
				", createDate=" + createDate + 
				", updateDate=" + updateDate + 
				", operator=" + operator + "]";
	}
	
}