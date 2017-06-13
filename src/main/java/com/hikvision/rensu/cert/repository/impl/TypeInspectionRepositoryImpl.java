package com.hikvision.rensu.cert.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;

@Repository
public class TypeInspectionRepositoryImpl extends SimpleJpaRepository<TypeInspection, Long> implements TypeInspectionRepository {

	private String[] tFields = { "model", "name", "testType", "basis", "docNo", "organization", "remarks" };
	
	private String[] cFields = { "caseDescription", "caseName","remarks"};
	
	
	public TypeInspectionRepositoryImpl(EntityManager em) {
		super(TypeInspection.class, em);
	}

	@SuppressWarnings("unchecked")
	public List<TypeInspection> findByDocNo(String docNo) {
		Query query = getEntityManager().createQuery("from TypeInspection where docNo=:docNo");
		query.setParameter("docNo", docNo);
		return query.getResultList();
	}

	public List<?> joinSearchTypeInspection(String fieldName, String[] keywords, String[] contentKeywords) {
		StringBuilder sqlString = new StringBuilder("SELECT T.id, T.model, T.name, T.docFilename, T.certUrl, T.docNo, C.caseName, C.caseDescription "
				+ "FROM TypeInspection T, InspectContent C WHERE T.id = C.inspectionId AND ");
		if(null!=keywords && keywords.length>0){
			sqlString.append("(");
			if (StringUtils.isBlank(fieldName)) {
				/* 循环查询typeInspection全部关键字段 */
				for (int i = 0; i < tFields.length; i++) {
					for (int j = 0; j < keywords.length; j++) {
						if (i > 0 || j > 0)
							sqlString.append(" OR ");
						sqlString.append("lower(T.").append(tFields[i]).append(")");
						sqlString.append(" LIKE ").append("lower('%").append(keywords[j]).append("%')");
					}
				}
			} else {
				/* 查询typeInspection的指定关键字段 */
				for (int j = 0; j < keywords.length; j++) {
					if (j > 0)
						sqlString.append(" OR ");
					sqlString.append("lower(T.").append(fieldName).append(")");
					sqlString.append(" LIKE ").append("lower('%").append(keywords[j]).append("%')");
				}
			}
			sqlString.append(")");
		}
		
		if(null!=contentKeywords && contentKeywords.length>0){
			sqlString.append(" AND (");
			/* 循环查询inspectContent全部三个关键字段 */
			for (int i = 0; i < cFields.length; i++) {
				for (int j = 0; j < contentKeywords.length; j++) {
					if (i > 0 || j > 0)
						sqlString.append(" OR ");
					sqlString.append("lower(C.").append(cFields[i]).append(")");
					sqlString.append(" LIKE ").append("lower('%").append(contentKeywords[j]).append("%')");
				}
			}
			sqlString.append(")");
		}
		System.out.println(sqlString);
		Query query = getEntityManager().createQuery(sqlString.toString());
		List<?> res = query.getResultList();
		return res;
	}

}
