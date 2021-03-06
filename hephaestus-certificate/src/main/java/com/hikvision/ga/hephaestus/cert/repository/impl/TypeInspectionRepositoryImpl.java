package com.hikvision.ga.hephaestus.cert.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.text.AbstractDocument.Content;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.cert.domain.InspectContent;
import com.hikvision.ga.hephaestus.cert.domain.TypeInspection;
import com.hikvision.ga.hephaestus.cert.repository.TypeInspectionRepository;
import com.hikvision.ga.hephaestus.cert.support.TypeSearchResult;

/**
 * @author langyicong
 *
 */
@Repository
public class TypeInspectionRepositoryImpl extends SimpleJpaRepository<TypeInspection, Long>
    implements TypeInspectionRepository {

  private String[] tFields =
      {"model", "name", "testType", "basis", "docNo", "organization", "remarks"};

  private String[] tFieldsAll = {"id", "version", "certUrl", "company", "awardDate", "model",
      "name", "testType", "basis", "docNo", "organization", "remarks"};

  private String[] cFields = {"caseDescription", "caseName", "remarks"};

  private String[] cFieldsAll = {"caseDescription", "caseName", "caseId", "remarks"};

  @Autowired
  @PersistenceContext
  private EntityManager entityManager;

  /**
   * @param em EntityManager
   */
  public TypeInspectionRepositoryImpl(EntityManager em) {
    super(TypeInspection.class, em);
  }
  
  @SuppressWarnings({"unchecked"})
  public List<TypeSearchResult> joinSearchTypeInspection(String fieldName, String[] keywords, String searchRelation,
      String[] contentKeywords, String contentKeywordsRelation) {
    StringBuilder sqlString = new StringBuilder("SELECT ");
    for (int i = 0; i < tFieldsAll.length; i++) {
      if (i > 0)
        sqlString.append(", ");
      sqlString.append("T." + toUnderline(tFieldsAll[i]));
    }
    /* 搜索报告内部信息 */
    sqlString.append(", C.id AS cid");
    for (int i = 0; i < cFieldsAll.length; i++) {
      if (StringUtils.equals(cFieldsAll[i], "remarks")) {
        sqlString.append(", C." + toUnderline(cFieldsAll[i]) + " AS tRemarks");
      } else {
        sqlString.append(", C." + toUnderline(cFieldsAll[i]));
      }
    }
    sqlString.append(
        " FROM type_inspection as T LEFT JOIN inspect_content as C ON T.id = C.inspection_id WHERE ");

    if (null != contentKeywords && contentKeywords.length > 0) {
      sqlString.append("((");
      /* 循环查询inspectContent全部三个关键字段 */
      for (int i = 0; i < cFields.length; i++) {
       if (i > 0)
         sqlString.append(") OR ("); //所有字段查询，字段间固定OR关系
       for (int j = 0; j < contentKeywords.length; j++) {
          if (j > 0)
            sqlString.append(" ").append(contentKeywordsRelation).append(" ");  //关键字之间关系由contentKeywordsRelation决定
          sqlString.append("lower(C.").append(toUnderline(cFields[i])).append(")");
          sqlString.append(" LIKE ").append("lower('%").append(contentKeywords[j]).append("%')");
        }
        //sqlString.append(")");
      }
      sqlString.append(")");
    }

    //如果关键字和内容关键字都不为空，用searchRelation逻辑连接查询语句
    if (null != keywords && keywords.length > 0) {
      if (null != contentKeywords && contentKeywords.length > 0) 
        sqlString.append(") ").append(searchRelation).append(" (");
      sqlString.append("(");
      if (StringUtils.isBlank(fieldName)) {
        /* 循环查询typeInspection全部关键字段 */
        for (int i = 0; i < tFields.length; i++) {
          if (i > 0)
            sqlString.append(") OR ("); //TODO 这里固定或关系不合理，可能要改为INTERSECT或者UNION语句 -lyc
          for (int j = 0; j < keywords.length; j++) {
            if (j > 0)
              sqlString.append(" OR "); //TODO 这里固定或关系不合理，可能要改为INTERSECT或者UNION语句 -lyc
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
      if (null != contentKeywords && contentKeywords.length > 0) 
        sqlString.append(")");
      sqlString.append(")");
    }else{
      sqlString.append(")");
    }

    // System.out.println(sqlString);
    Query query = entityManager.createNativeQuery(sqlString.toString());
    List<Object[]> resObj = query.getResultList();
    List<TypeSearchResult> searchResults = new ArrayList<TypeSearchResult>();
    if (resObj.size() > 0) {
      TypeSearchResult s = new TypeSearchResult();
      List<InspectContent> cList = new ArrayList<InspectContent>();
      InspectContent c = new InspectContent();
      Long tId = -1L; // 存放上轮id
      int count = 0; // 存放搜索命中计数
      StringBuilder thisCase = new StringBuilder();
      for (Object[] objects : resObj) {
        //对于每条检索结果
/*        int i = 0;
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
        System.out.println(objects[i++].toString()); // 11:remarks 空
        System.out.println(objects[i++].toString()); // 12:content Id 
        System.out.println(objects[i++].toString()); // 13:caseDescription
        System.out.println(objects[i++].toString()); // 14:caseName
        System.out.println(objects[i++].toString()); // 15:caseId
        System.out.println(objects[i++]); // 15:caseId
*/
        /* 如果搜索结果中的型检项Id与上轮不同 */
        if (tId != Long.parseLong(objects[0].toString())) {
          if (tId > 0L) { // 排除第一个
            s.setContentList(cList);
            s.setCases(new String(thisCase.toString())); // 上一轮的thisCase
            s.setCount(count); // 上一轮count
            searchResults.add(s); // 上个typeSearchResult对象存入列表
            /* another typeSearchResult */
            s = new TypeSearchResult();
            cList = new ArrayList<InspectContent>();
            thisCase.setLength(0); // 清空thisCase
            count = 0;
          }
          tId = Long.parseLong(objects[0].toString()); // 暂存本轮tId
          /*设置型检条目内容*/
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
          s.setTypeInspection(t);  //t放入结果中
          t=null; //让jvm回收t
        }
        // 检测项内容如果存在(content Id!=null)
        if (null != objects[12]) {
          /*表格风格*/
          c.setCaseDescription(objects[13].toString());
          c.setCaseName(objects[14].toString());
          c.setCaseId(objects[15].toString()); 
          c.setRemarks(objects[16]==null? "":objects[16].toString());
          cList.add(c);
          c = new InspectContent();
          //s.addContent(c); //TODO debug 对象引用问题导致前面放入list中的对象也被覆盖了
          /*文本流风格*/
          thisCase.append(c.getCaseId() + " " + StringUtils.replaceAll(c.getCaseName(), "\n", " ") + " "
              + objects[13].toString() + " \n ");
        }
        count++;
      }
      s.setContentList(cList);
      s.setCases(new String(thisCase.toString())); // 最后一轮的thisCase
      s.setCount(count); // 最后一轮count
      searchResults.add(s); // 最后一个typeSearchResult对象存入列表
    }
    /*按照命中率排序*/
    Collections.sort(searchResults, new Comparator<TypeSearchResult>() {
      public int compare(TypeSearchResult t1, TypeSearchResult t2) {
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
