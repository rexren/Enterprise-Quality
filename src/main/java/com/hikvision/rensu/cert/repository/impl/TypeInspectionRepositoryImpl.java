package com.hikvision.rensu.cert.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;
import com.hikvision.rensu.cert.support.typeSearchResult;

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

	@SuppressWarnings({ "unchecked" })
	public List<?> joinSearchTypeInspection(String fieldName, String[] keywords, String[] contentKeywords) {
		StringBuilder sqlString = new StringBuilder("SELECT T.id, ");
		for (int i = 0; i < tFields.length; i++) {
			if (i > 0)
				sqlString.append(", ");
			sqlString.append("T." + toUnderline(tFields[i]));
		}
		sqlString.append(", C.id AS cid");
		for (int i = 0; i < cFields.length; i++) {
			if (StringUtils.equals(cFields[i], "remarks")) {
				sqlString.append(", C." + toUnderline(cFields[i]) + " AS tRemarks");
			} else {
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
				sqlString.append(" AND ");
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
		List<Object[]> resObj = query.getResultList();
		List<typeSearchResult> searchResults = new ArrayList<typeSearchResult>();
		if (resObj.size() > 0) {
			typeSearchResult s = new typeSearchResult();
			Long tId = -1L; // 存放上轮id
			int count = 0; // 存放搜索命中计数
			StringBuilder thisCase = new StringBuilder();
			for (Object[] objects : resObj) {
				/*
				int i = 0;
				System.out.println(objects[i++].toString()); // 0:id
				System.out.println(objects[i++].toString()); // 1:model
				System.out.println(objects[i++].toString()); // 2:name
				System.out.println(objects[i++].toString()); // 3:testType
				System.out.println(objects[i++].toString()); // 4:basis
				System.out.println(objects[i++].toString()); // 5:docNo
				System.out.println(objects[i++].toString()); // 6:organization
				System.out.println(objects[i++].toString()); // 7:remarks
				System.out.println(objects[i++].toString()); // 8:contentId
				System.out.println(objects[i++].toString()); // 9:caseDescription
				System.out.println(objects[i++].toString()); // 10:caseName
				*/				
				if (tId != Long.parseLong(objects[0].toString())) {
					if (tId > 0L) {
						s.setCases(new String(thisCase.toString())); // 上一轮的thisCase
						s.setCount(count); // 上一轮count
						searchResults.add(s); // 上个typeSearchResult对象存入列表
						/* another typeSearchResult */
						s = new typeSearchResult();
						thisCase.setLength(0); // 清空thisCase
						count = 0;
					}
					tId = Long.parseLong(objects[0].toString());
					s.setId(tId);
					s.setModel(objects[1].toString());
					s.setName(objects[2].toString());
					s.setTestType(objects[3].toString());
					s.setBasis(objects[4].toString());
					s.setDocNo(objects[5].toString());
					s.setOrganization(objects[6].toString());
					s.setRemarks(objects[7].toString());
				}
				if(null!=objects[9]){
					thisCase.append(StringUtils.replaceAll(objects[10].toString(),"\n"," ") +" "+ objects[9].toString()+" || ");
				}
				count++;
			}
			s.setCases(new String(thisCase.toString())); // 最后一轮的thisCase
			s.setCount(count);    // 最后一轮count
			searchResults.add(s); // 最后一个typeSearchResult对象存入列表
		}
		Collections.sort(searchResults, new Comparator<typeSearchResult>() {
			public int compare(typeSearchResult t1, typeSearchResult t2){
				return t1.getCount()>t2.getCount()?-1:t1.getCount()<t2.getCount()?1:0;
			}
		});
		return searchResults;
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
