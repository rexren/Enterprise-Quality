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
public class TypeInspectionRepositoryImpl extends SimpleJpaRepository<TypeInspection, Long>
		implements TypeInspectionRepository {

	private String[] tFields = { "model", "name", "testType", "basis", "docNo", "organization", "remarks" };

	private String[] cFields = { "caseDescription", "caseName", "remarks" };

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
		StringBuilder sqlString = new StringBuilder("SELECT ");
		//TODO DISTINCT(T.id) 或者T.id, C.id
		for (int i = 0; i < tFields.length; i++) {
			if (i > 0)
				sqlString.append(", ");
			sqlString.append("T." + toUnderline(tFields[i]));
		}
		for (int i = 0; i < cFields.length; i++) {
			if(StringUtils.equals(cFields[i], "remarks")){
				sqlString.append(", C." + toUnderline(cFields[i]) + " AS tRemarks");
			} else{
				sqlString.append(", C." + toUnderline(cFields[i]));
			}
		}
		sqlString.append(" FROM type_inspection as T LEFT JOIN inspect_content as C ON T.id = C.inspection_id WHERE ");
		if (null != contentKeywords && contentKeywords.length > 0) {
			sqlString.append("(");
			/* 循环查询inspectContent全部三个关键字段 */
			for (int i = 0; i < cFields.length; i++) {
				for (int j = 0; j < contentKeywords.length; j++) {
					if (i > 0 || j > 0)
						sqlString.append(" OR ");
					sqlString.append("lower(C.").append(toUnderline(cFields[i])).append(")");
					sqlString.append(" LIKE ").append("lower('%").append(contentKeywords[j]).append("%')");
				}
			}
			sqlString.append(")");
		}

		if (null != keywords && keywords.length > 0) {
			if (null != contentKeywords && contentKeywords.length > 0) {
				sqlString.append("AND ");
			}
			sqlString.append("(");
			if (StringUtils.isBlank(fieldName)) {
				/* 循环查询typeInspection全部关键字段 */
				for (int i = 0; i < tFields.length; i++) {
					for (int j = 0; j < keywords.length; j++) {
						if (i > 0 || j > 0)
							sqlString.append(" OR ");
						sqlString.append("lower(T.").append(toUnderline(tFields[i])).append(")");
						sqlString.append(" LIKE ").append("lower('%").append(keywords[j]).append("%')");
					}
				}
			} else {
				/* 查询typeInspection的指定关键字段 */
				for (int j = 0; j < keywords.length; j++) {
					if (j > 0)
						sqlString.append(" OR ");
					sqlString.append("lower(T.").append(toUnderline(fieldName)).append(")");
					sqlString.append(" LIKE ").append("lower('%").append(keywords[j]).append("%')");
				}
			}
			sqlString.append(")");
		}

		System.out.println(sqlString);
		Query query = getEntityManager().createNativeQuery(sqlString.toString());
		List<?> res = query.getResultList();
		//TODO new a class to save the List, and order by 匹配相似度（条目数）
		return res;
	}

	private String toUnderline(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i >= 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					if (i > 0)
						sb.append('_');
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

}
