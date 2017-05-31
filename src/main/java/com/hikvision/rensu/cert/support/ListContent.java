package com.hikvision.rensu.cert.support;

import java.util.List;

/**
 * Created by langyicong on 2017/5/31.
 */
public class ListContent <T> {
	private int pageSize;
	private Long totalElements;
	private int totalPages;
	private List<T> list;
	
	public ListContent() {
	}
	
	public ListContent(int pageSize, Long totalElements, int totalPages, List<T> list) {
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

	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
}
