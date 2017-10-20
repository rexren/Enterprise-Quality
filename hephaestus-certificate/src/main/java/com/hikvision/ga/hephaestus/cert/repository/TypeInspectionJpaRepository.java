package com.hikvision.ga.hephaestus.cert.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.cert.domain.TypeInspection;

/**
 * Created by rensu on 2017/4/22.
 */
@Repository
public interface TypeInspectionJpaRepository extends JpaRepository<TypeInspection, Long> {

  /**
   * @param docNo
   * @param pageable 
   * @return 符合条件的TypeInspection列表
   */
  public List<TypeInspection> findByDocNo(String docNo);

  /**
   * 按照颁发日期筛选
   * @param start 开始时间
   * @param end 结束时间
   * @param pageable 分页对象
   * @return 符合时间筛选条件的TypeInspection列表
   */
  @Query(value="from TypeInspection t where t.awardDate>=:start AND t.awardDate<=:end ORDER BY awardDate DESC")
  public Page<TypeInspection> findTypeInspectionByTimeRange(@Param("start") Date start, @Param("end") Date end, Pageable pageable);

}
