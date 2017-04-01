package com.hikvision.rensu.cert.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rensu on 17/3/25.
 */
@Entity
public class Cert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 维护人员
     */
    @Column(nullable = false)
    private String maintenaner;

    /**
     * 资质名称
     */
    @Column(nullable = false)
    private String name;


    /**
     * 资质类型: 1:公检&国标 2:双证 3:3C
     */
    @Column(nullable = false)
    private int type;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private Date createAt;


    /**
     * 更新时间
     */
    @Column(nullable = false)
    private Date updateAt;

    @SuppressWarnings("unused")
    private Cert() {
    }


}
