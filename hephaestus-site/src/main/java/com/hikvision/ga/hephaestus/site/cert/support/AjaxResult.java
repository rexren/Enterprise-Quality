package com.hikvision.ga.hephaestus.site.cert.support;

public class AjaxResult<T> extends BaseResult{
	private T data;

	public AjaxResult() {
		super();
	}

	public AjaxResult(T data) {
		super();
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
