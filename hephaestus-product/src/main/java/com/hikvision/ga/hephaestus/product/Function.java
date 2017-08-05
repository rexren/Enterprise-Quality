package com.hikvision.ga.hephaestus.product;

import java.io.Serializable;

/**
 * Created by rensu on 2017/7/19.
 */
public class Function implements Serializable{

    private Long id;
    //从属哪个组件
    private Long componentId;
    //主类别
    private String mainClass;
    //类别1
    private String class1;
    //类别2
    private String class2;
    //名称
    private String name;
    //备注
    private String remark;
    //起始版本
    private String from;
}
