package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.TypeInspection;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rensu on 17/4/21.
 */
@Service
public class TypeInspectionService {
    public List<TypeInspection> getInspections() {

        List<TypeInspection> typeInspectionList = Arrays.asList(
                new TypeInspection("iVMS-8200", "海康威视iVMS-8200公安集中监控管理系统软件", new Date(), "110155110431", "http://cert.hikvision.com.cn/HikCert/detail?certid=r37289oqhfew97f3ho"),
                new TypeInspection("iVMS-9500E", "可视化实战应用系统软件", new Date(), "公沪检138771", ""));
        return typeInspectionList;
    }
}
