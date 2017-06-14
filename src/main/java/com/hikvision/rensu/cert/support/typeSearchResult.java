package com.hikvision.rensu.cert.support;

public class typeSearchResult {
	private long id;
	private String model;
	private String name;
	private String testType;
	private String basis;
	private String docNo;
	private String organization;
	private String remarks;
	private int count;
	private String cases;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getBasis() {
		return basis;
	}

	public void setBasis(String basis) {
		this.basis = basis;
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public typeSearchResult() {
		super();
	}

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public typeSearchResult(long id, String model, String name, String testType, String basis, String docNo,
			String organization, String remarks, int count, String cases) {
		super();
		this.id = id;
		this.model = model;
		this.name = name;
		this.testType = testType;
		this.basis = basis;
		this.docNo = docNo;
		this.organization = organization;
		this.remarks = remarks;
		this.count = count;
		this.cases = cases;
	}

	public void empty(){
		this.id = 0L;
		this.model = null;
		this.name = null;
		this.testType = null;
		this.basis = null;
		this.docNo = null;
		this.organization = null;
		this.remarks = null;
		this.count = 0;
		this.cases = null;
	}
	
}
