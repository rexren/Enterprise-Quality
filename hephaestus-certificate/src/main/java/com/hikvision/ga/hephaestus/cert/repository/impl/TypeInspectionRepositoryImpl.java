package com.hikvision.ga.hephaestus.cert.repository.impl;

import com.hikvision.ga.hephaestus.cert.domain.TypeInspection;
import com.hikvision.ga.hephaestus.cert.domain.typeSearchResult;
import com.hikvision.ga.hephaestus.cert.repository.TypeInspectionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository
public class TypeInspectionRepositoryImpl extends SimpleJpaRepository<TypeInspection, Long>
        implements TypeInspectionRepository {

    private String[] tFields = {"model", "name", "testType", "basis", "docNo", "organization", "remarks"};

    private String[] tFieldsAll = {"id", "version", "certUrl", "company", "awardDate", "model", "name", "testType", "basis", "docNo", "organization", "remarks"};

    private String[] cFields = {"caseDescription", "caseName", "remarks"};
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    public TypeInspectionRepositoryImpl(EntityManager em) {
        super(TypeInspection.class, em);
    }

    @SuppressWarnings("unchecked")
    public List<TypeInspection> findByDocNo(String docNo) {
        Query query = entityManager.createQuery("from TypeInspection where docNo=:docNo");
        query.setParameter("docNo", docNo);
        return query.getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<typeSearchResult> joinSearchTypeInspection(String fieldName, String[] keywords, String[] contentKeywords) {
        StringBuilder sqlString = new StringBuilder("SELECT ");
        for (int i = 0; i < tFieldsAll.length; i++) {
            if (i > 0)
                sqlString.append(", ");
            sqlString.append("T." + toUnderline(tFieldsAll[i]));
        }
        /*搜索报告内部信息*/
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
        Query query = entityManager.createNativeQuery(sqlString.toString());
        List<Object[]> resObj = query.getResultList();
        List<typeSearchResult> searchResults = new ArrayList<typeSearchResult>();
        if (resObj.size() > 0) {
            typeSearchResult s = new typeSearchResult();
            Long tId = -1L; // 存放上轮id
            int count = 0; // 存放搜索命中计数
            StringBuilder thisCase = new StringBuilder();
            for (Object[] objects : resObj) {
                /* int i = 0;
				System.out.println(objects[i++].toString()); // 0:id
				System.out.println(objects[i++].toString()); // 1:version
				System.out.println(objects[i++].toString()); // 2:certUrl
				System.out.println(objects[i++].toString()); // 3:company
				System.out.println(objects[i++].toString()); // 4:(Date)awardDate
				System.out.println(objects[i++].toString()); // 5:model
				System.out.println(objects[i++].toString()); // 6:name
				System.out.println(objects[i++].toString()); // 7:testType
				System.out.println(objects[i++].toString()); // 8:basis
				System.out.println(objects[i++].toString()); // 9:docNo
				System.out.println(objects[i++].toString()); // 10:organization
				System.out.println(objects[i++].toString()); // 11:remarks
				System.out.println(objects[i++].toString()); // 12:contentId
				System.out.println(objects[i++].toString()); // 13:caseDescription
				System.out.println(objects[i++].toString()); // 14:caseName
				*/
				
				/* 如果搜索结果中的tId与上轮不同*/
                if (tId != Long.parseLong(objects[0].toString())) {
                    if (tId > 0L) { //排除第一个
                        s.setCases(new String(thisCase.toString())); // 上一轮的thisCase
                        s.setCount(count); // 上一轮count
                        searchResults.add(s); // 上个typeSearchResult对象存入列表
						/* another typeSearchResult */
                        s = new typeSearchResult();
                        thisCase.setLength(0); // 清空thisCase
                        count = 0;
                    }
                    tId = Long.parseLong(objects[0].toString());  //暂存本轮tId
                    TypeInspection t = new TypeInspection();
                    t.setId(tId);
                    t.setVersion(objects[1].toString());
                    t.setCertUrl(objects[2].toString());
                    t.setCompany(objects[3].toString());
                    t.setAwardDate((Date) objects[4]);
                    t.setModel(objects[5].toString());
                    t.setName(objects[6].toString());
                    t.setTestType(objects[7].toString());
                    t.setBasis(objects[8].toString());
                    t.setDocNo(objects[9].toString());
                    t.setOrganization(objects[10].toString());
                    t.setRemarks(objects[11].toString());
                    s.setTypeInspection(t);
                }
                if (null != objects[13]) {
                    thisCase.append(StringUtils.replaceAll(objects[14].toString(), "\n", " ") + " " + objects[13].toString() + " || ");
                }
                count++;
            }
            s.setCases(new String(thisCase.toString())); // 最后一轮的thisCase
            s.setCount(count);    // 最后一轮count
            searchResults.add(s); // 最后一个typeSearchResult对象存入列表
        }
        Collections.sort(searchResults, new Comparator<typeSearchResult>() {
            public int compare(typeSearchResult t1, typeSearchResult t2) {
                return t1.getCount() > t2.getCount() ? -1 : t1.getCount() < t2.getCount() ? 1 : 0;
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