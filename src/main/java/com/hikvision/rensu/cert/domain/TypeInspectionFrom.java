package com.hikvision.rensu.cert.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by rensu on 17/4/25.
 * 创建type inspection 表单
 */
public class TypeInspectionFrom {
    private static final long serialVersionUID = -1L;

    private Long id;

    /**
     * 维护人员
     */
    private String maintenaner;

    /**
     * 产品型号
     */
    private String productType;

    /**
     * 软件名称
     */
    private String name;

    /**
     * 软件版本
     */
    private String version;


    /**
     * 测试/检验类型
     */
    private String testType;

    /**
     * 受检单位
     */
    private String company;

    /**
     * 测试依据
     */
    private String basis;

    /**
     * 颁发日期
     */
    private String awardDate;

    /**
     * 文件编号
     */
    private String docSerial;

    /**
     * 证书系统链接
     */
    private String certUrl;

    /**
     * 认证机构
     */
    private String testOrgnization;

    /**
     * 备注
     */
    private String remarks;


    private TypeInspectionFrom() {
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

    public String getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(String awardDate) {
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
                '}';
    }

    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public TypeInspection transfrom() {
        TypeInspection ti = new TypeInspection();
        ti.setMaintenaner(this.maintenaner);
        ti.setProductType(this.productType);
        ti.setName(this.name);
        ti.setVersion(this.version);
        ti.setTestType(this.testType);
        ti.setCompany(this.company);
        ti.setBasis(this.basis);
        try {
            ti.setAwardDate(sdf.parse(this.awardDate));
        }catch (ParseException e) {
            e.printStackTrace();
        }
        ti.setDocSerial(this.docSerial);
        ti.setCertUrl(this.certUrl);
        ti.setTestOrgnization(this.testOrgnization);
        ti.setRemarks(this.remarks);
        return ti;
    }
}
