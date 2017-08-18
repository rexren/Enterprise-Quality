package com.hikvision.ga.hephaestus.site.controller.vo;

public class ImportResult extends BaseResult{
	private int numOfInspections;
	private int numOfCopyRight;
	private int numOf3C;

	public ImportResult() {
		super();
	}

	public ImportResult(String code, String msg) {
		super(code, msg);
		this.numOfInspections=0;
		this.numOfCopyRight=0;
		this.numOf3C=0;
	}

	public ImportResult(String code, String msg, int numOfInspections, int numOfCopyRight, int numOf3C) {
		super(code, msg);
		this.numOfInspections=numOfInspections;
		this.numOfCopyRight=numOfCopyRight;
		this.numOf3C=numOf3C;
	}

	public int getNumOfInspections() {
		return numOfInspections;
	}

	public void setNumOfInspections(int numOfInspections) {
		this.numOfInspections = numOfInspections;
	}

	public int getNumOfCopyRight() {
		return numOfCopyRight;
	}

	public void setNumOfCopyRight(int numOfCopyRight) {
		this.numOfCopyRight = numOfCopyRight;
	}

	public int getNumOf3C() {
		return numOf3C;
	}

	public void setNumOf3C(int numOf3C) {
		this.numOf3C = numOf3C;
	}

}
