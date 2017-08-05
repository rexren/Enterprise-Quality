package com.hikvision.ga.hephaestus.site.cert.support;

import java.util.List;

/**
 * Created by langyicong on 2017/5/31.
 */
public class ListContent {
	private int pageSize;
	private Long totalElements;
	private int totalPages;
	private List<?> list;
	
	public ListContent() {
	}
	
	public ListContent(int pageSize, Long totalElements, int totalPages, List<?> list) {
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.list = list;
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	
}
