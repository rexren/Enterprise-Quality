package com.hikvision.ga.hephaestus.product;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * Created by rensu on 2017/7/19.
 */
@Entity
public class Component implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1L;

    @Id
    private Long id;

    //全名
    private String name;
    //缩写
    private String abbreviaion;
    //版本
    private String version;
    //包含的组件
    private List<Component>  components;
}
