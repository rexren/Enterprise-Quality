package com.hikvision.ga.hephaestus.cert.support;


import java.util.ArrayList;
import java.util.List;

import com.hikvision.ga.hephaestus.cert.domain.InspectContent;
import com.hikvision.ga.hephaestus.cert.domain.TypeInspection;

/**
 * @author langyicong 内容搜索的返回值
 *
 */
public class TypeSearchResult {

  private TypeInspection typeInspection;

  private int count;

  private String cases;

  private List<InspectContent> contentList;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getCases() {
    return cases;
  }

  public void setCases(String cases) {
    this.cases = cases;
  }

  public TypeInspection getTypeInspection() {
    return typeInspection;
  }

  public void setTypeInspection(TypeInspection typeInspection) {
    this.typeInspection = typeInspection;
  }


  public TypeSearchResult() {
    super();
  }


  public List<InspectContent> getContentList() {
    return contentList;
  }

  public TypeSearchResult(TypeInspection typeInspection, int count, String cases,
      List<InspectContent> contentList) {
    super();
    this.typeInspection = typeInspection;
    this.count = count;
    this.cases = cases;
    this.contentList = contentList;
  }

  /**
   * 获取按照caseId排序的列表
   * 
   * @param dir 递增(AESC)或者递减(DESC)的顺序
   * @return 排序后的contentList
   */
  public List<InspectContent> getOrderedContentList(String dir) {
    // TODO:SORT
    return contentList;
  }

  public void setContentList(List<InspectContent> contentList) {
    this.contentList = contentList;
  }

  /**
   * 增加一条检测内容
   * @param c 检测内容项
   * @return
   */
  public List<InspectContent> addContent(InspectContent c) {
    if(this.contentList==null){
      this.contentList = new ArrayList<InspectContent>();
    }
    this.contentList.add(c);
    return this.contentList;
  }


}
