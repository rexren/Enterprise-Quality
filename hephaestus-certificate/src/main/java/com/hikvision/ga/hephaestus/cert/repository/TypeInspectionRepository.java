package com.hikvision.ga.hephaestus.cert.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.ga.hephaestus.cert.domain.TypeInspection;
import com.hikvision.ga.hephaestus.cert.domain.typeSearchResult;

/**
 * Created by rensu on 2017/4/22.
 */
@NoRepositoryBean
public interface TypeInspectionRepository extends JpaRepository<TypeInspection, Long> {

  //public List<TypeInspection> findByDocNo(String docNo);

  /**
   * 关键字联合查询，并且按照命中率排序
   * @param fieldName 查询的域
   * @param keywordList 资质的关键字列表
   * @param searchRelation 关键词检索和内容检索之间的关系，“或”或者“与”，默认为“与”
   * @param contentKeywordList 资质报告的内容关键字列表
   * @param contentKeywordRelation 搜索内容关键字的关系，“或”或者“与”，默认为“与”
   * @return 搜索结果列表
   */
  public List<typeSearchResult> joinSearchTypeInspection(String fieldName, String[] keywordList,String searchRelation,
      String[] contentKeywordList, String contentKeywordRelation);

  /**
   * 按照颁发日期筛选
   * @param start
   * @param end
   * @return 符合条件的TypeInspection列表
   */
  public List<TypeInspection> findTypeInspectionByTimeRange(Date start, Date end);

}
