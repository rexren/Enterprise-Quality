package com.hikvision.ga.hephaestus.cert.domain;


/**
 * @author langyicong
 *         内容搜索的返回值
 */
public class typeSearchResult {

    TypeInspection typeInspection;

    private int count;
    private String cases;

    public typeSearchResult() {
        super();
    }

    public typeSearchResult(TypeInspection typeInspection, int count, String cases) {
        super();
        this.typeInspection = typeInspection;
        this.count = count;
        this.cases = cases;
    }

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

}
