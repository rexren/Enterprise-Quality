package com.hikvision.rensu.cert.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.CccPage;
import com.hikvision.rensu.cert.repository.CccPageRepository;

@Repository
public class CccPageRepositoryImpl extends SimpleJpaRepository<CccPage, Long> implements CccPageRepository {

	private String[] fields = { "model", "productName", "docNo", "remarks" };

	@Autowired
	public CccPageRepositoryImpl (EntityManager em) {
		super(CccPage.class, em);
	}

	@SuppressWarnings("unchecked")
	public List<CccPage> findByDocNo(String docNo) {
		// Query query = em.createQuery("from CccPage where docNo=:docNo");
		Query query = getEntityManager().createQuery("from CccPage where docNo=:docNo");
		query.setParameter("docNo", docNo);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CccPage> searchCccByKeyword(String fieldName, String[] keywords) {
		StringBuilder sqlString = new StringBuilder("FROM CccPage where ");
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

		// Query query = em.createQuery(sqlString.toString());
		Query query = getEntityManager().createQuery(sqlString.toString());
		List<CccPage> res = query.getResultList();
		return res;
	}

}
