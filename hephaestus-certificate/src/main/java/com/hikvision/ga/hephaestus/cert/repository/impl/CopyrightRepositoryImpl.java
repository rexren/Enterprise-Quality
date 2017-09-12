package com.hikvision.ga.hephaestus.cert.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hikvision.ga.hephaestus.cert.Copyright;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.cert.repository.CopyrightRepository;

@Repository
public class CopyrightRepositoryImpl extends SimpleJpaRepository<Copyright, Long>
    implements CopyrightRepository {

  @Autowired
  @PersistenceContext
  private EntityManager em;

  private String[] fields =
      {"softwareName", "abbreviation", "model", "crNo", "rgNo", "epNo", "cdNo"};

  public CopyrightRepositoryImpl(EntityManager em) {
    super(Copyright.class, em);
  }

  @SuppressWarnings("unchecked")
  public List<Copyright> findBySoftwareName(String softwareName) {
    Query query = em.createQuery("from Copyright where softwareName=:softwareName");
    query.setParameter("softwareName", softwareName);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Copyright> searchCopyrightByKeyword(String fieldName, String[] keywords) {
    StringBuilder sqlString = new StringBuilder("FROM Copyright where ");
    if (StringUtils.isBlank(fieldName)) {
      // 循环查询全部关键字段
      for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < keywords.length; j++) {
          if (i > 0 || j > 0)
            sqlString.append(" OR ");
          sqlString.append("lower(").append(fields[i]).append(")");
          sqlString.append(" LIKE ").append("lower('%").append(keywords[j]).append("%')");
        }
      }
    } else {
      for (int j = 0; j < keywords.length; j++) {
        if (j > 0)
          sqlString.append(" OR ");
        sqlString.append("lower(").append(fieldName).append(")");
        sqlString.append(" LIKE ").append("lower('%").append(keywords[j]).append("%')");
      }
    }
    System.out.println(sqlString.toString());

    Query query = em.createQuery(sqlString.toString());
    List<Copyright> res = query.getResultList();
    return res;
  }
}
