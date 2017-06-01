package com.hikvision.rensu.cert.constant;

public enum RetStatus {
	/* 请求成功 */
	SUCCESS("0", "成功"), 
	
	/* 请求数据格式（预留字段1000-1099） */
	FORM_DATA_INVALID("1001", "表单验证不通过"),
	FORM_DATA_MISSING("1002", "表单验证缺少关键字段"),
	DOCNO_DUPLICATED("1004", "docNo字段重复"),
	
	/* 用户登录/权限错误（预留字段1100-1199） */
	USER_NOT_LOGGED_IN("1101", "用户未登录"),
	USER_AUTH_ERROR("1102","用户权限不足"),
	
	/* 文件解析错误（预留字段1200-1299） */
	/* 1200-1209：文件自身格式问题 */
	FILE_EMPTY("1201","文件为空"),
	FILE_ENCYPTED("1202","文件被加密"),
	FILE_SHEET_MISSING("1210","找不到符合条件的工作表"),
	FILE_INVALID("1211","文件无效"),
	FILE_KEYWORD_ERROR("1212","表格中关键字错误"),
	FILE_PARSING_ERROR("1220","文件解析错误"),
	
	/* 系统(数据库)异常 */
	SYSTEM_ERROR("1301","系统异常"),
	ITEM_NOT_FOUND("1302","数据库中找不到数据");
	
	private String code;
	private String info;
	
	private RetStatus(String code, String info) {
		this.code = code;
		this.info = info;
	}

	public String getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}

}
