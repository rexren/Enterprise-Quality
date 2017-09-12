package com.hikvision.ga.hephaestus.cert.repository;

import java.util.List;

import com.hikvision.ga.hephaestus.cert.support.TypeSearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import com.hikvision.ga.hephaestus.cert.TypeInspection;

/**
 * Created by rensu on 2017/4/22.
 */
@NoRepositoryBean
public interface TypeInspectionRepository extends JpaRepository<TypeInspection, Long> {

    public List<TypeInspection> findByDocNo(String docNo);

    /**
     * 关键字联合查询，并且按照命中率排序
     */
    public List<TypeSearchResult> joinSearchTypeInspection(String fieldName, String[] keywordList, String[] contentKeywordList);

}
