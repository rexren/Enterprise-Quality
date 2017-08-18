package com.hikvision.ga.hephaestus.site.controller.vo;

public class ListResult extends BaseResult {
	private ListContent list; //返回的数据内容

	public ListContent getListContent() {
		return list;
	}

	public void setListContent(ListContent list) {
		this.list = list;
	}

	public ListResult() {
		super();
	}

	public ListResult(ListContent list) {
		super();
		this.list = list;
	}
	
}
