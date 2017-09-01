package com.hikvision.ga.hephaestus.site.logger.repository.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.site.logger.OperationLog;
import com.hikvision.ga.hephaestus.site.logger.repository.OperationLogRepository;

/**
 * 日志Repository实现类
 * 
 * @author langyicong
 *
 */
@Repository
public class OperationLogRepositoryImpl extends SimpleJpaRepository<OperationLog, Long>
    implements OperationLogRepository {

  @Autowired
  @PersistenceContext
  private EntityManager entityManager;

  /**
   * 构造函数
   * 
   * @param em entityManager
   */
  public OperationLogRepositoryImpl(EntityManager em) {
    super(OperationLog.class, em);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<OperationLog> findLogByTimeRange(Date start, Date end) {
    Query query = entityManager.createQuery(
        "from OperationLog where createTime>=:start AND createTime<=:end ORDER BY createTime DESC");
    query.setParameter("start", start);
    Date next = new Date(end.getTime() + 1000 * 60 * 60 * 24 - 1);
    query.setParameter("end", next); // 包括当天时间
    return query.getResultList();
  }

}
