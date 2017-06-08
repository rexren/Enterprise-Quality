package com.hikvision.rensu.cert.repository;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.CccPage;
import com.hikvision.rensu.cert.repository.base.BasicRepository;

@Repository
public interface CccPageRepository extends BasicRepository<CccPage, Long> {

	static String[] fields = { "model", "productName", "docNo", "remarks" };

	@SuppressWarnings("unchecked")
	default List<CccPage> findByDocNo(String docNo) {
		Query query = getEntityManager().createQuery("from CccPage where docNo=:docNo");
		query.setParameter("docNo", docNo);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	default List<CccPage> searchCccByKeyword(String fieldName, String[] keywords) {
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

		Query query = getEntityManager().createQuery(sqlString.toString());
		List<CccPage> res = query.getResultList();
		return res;
	}
}
