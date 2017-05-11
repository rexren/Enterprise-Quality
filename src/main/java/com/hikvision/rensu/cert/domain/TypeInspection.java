package com.hikvision.rensu.cert.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 形式检测
 * Created by rensu on 17/3/25.
 */
@Entity
public class TypeInspection implements Serializable{

    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 维护人员
     */
    @Column
    private String maintenaner;

    /**
     * 产品型号
     */
    @Column(nullable = false)
    private String productType;


    /**
     * 软件名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 软件版本
     */
    @Column
    private String version;


    /**
     * 测试/检验类型
     */
    @Column
    private String testType;

    /**
     * 受检单位
     */
    @Column
    private String company;

    /**
     * 测试依据
     */
    @Column
    private String basis;

    /**
     * 颁发日期
     */
    @Column(nullable = false)
    private Date awardDate;

    /**
     * 文件编号
     */
    @Column(nullable = false)
    private String docSerial;

    /**
     * 证书系统链接
     */
    @Column
    private String certUrl;

    /**
     * 认证机构
     */
    @Column(nullable = false)
    private String testOrgnization;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    @Column
    private Date createAt;


    /**
     * 更新时间
     */
    @Column
    private Date updateAt;

    public TypeInspection(){
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaintenaner() {
        return maintenaner;
    }

    public void setMaintenaner(String maintenaner) {
        this.maintenaner = maintenaner;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public Date getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(Date awardDate) {
        this.awardDate = awardDate;
    }

    public String getDocSerial() {
        return docSerial;
    }

    public void setDocSerial(String docSerial) {
        this.docSerial = docSerial;
    }

    public String getCertUrl() {
        return certUrl;
    }

    public void setCertUrl(String certUrl) {
        this.certUrl = certUrl;
    }

    public String getTestOrgnization() {
        return testOrgnization;
    }

    public void setTestOrgnization(String testOrgnization) {
        this.testOrgnization = testOrgnization;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }



    public Date getUpdateAt() {
		return updateAt;
	}


	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}


	@Override
    public String toString() {
        return "TypeInspection{" +
                "id=" + id +
                ", maintenaner='" + maintenaner + '\'' +
                ", productType='" + productType + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", testType='" + testType + '\'' +
                ", company='" + company + '\'' +
                ", basis='" + basis + '\'' +
                ", awardDate=" + awardDate +
                ", docSerial='" + docSerial + '\'' +
                ", certUrl='" + certUrl + '\'' +
                ", testOrgnization='" + testOrgnization + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
