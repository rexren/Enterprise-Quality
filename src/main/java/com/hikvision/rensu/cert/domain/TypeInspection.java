package com.hikvision.rensu.cert.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 形式检测
 * Created by rensu on 17/3/25.
 */
@Entity
public class TypeInspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private Date awardDate;

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
}
