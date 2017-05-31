package com.hikvision.rensu.cert.constant;

public class RetCode {
	
	
	/* 请求成功 */
	public final static String SUCCESS_CODE = "0"; // 成功
	public final static String SUCCESS_INFO = "Success";

	/* 请求数据格式（预留字段1000-1099） */
	public final static String FORM_VALIDATE_ERROR_CODE = "1001"; // 提交的表单不符合要求 401
	public final static String FORM_VALIDATE_ERROR_INFO = "Form data is invalid";

	/* 用户登录/权限错误（预留字段1100-1199） */
	public final static String USER_NOT_LOGGED_IN_CODE = "1101"; // 用户未登录或者登陆过期
	public final static String USER_NOT_LOGGED_IN_INFO = "User not logged in";

	public final static String USER_AUTH_ERROR_CODE = "1102"; // 用户权限不足
	public final static String USER_AUTH_ERROR_INFO = "User not authorized";

	/* 文件解析错误（预留字段1200-1299） */
	/* 1200-1209：文件自身格式问题 */
	public final static String FILE_EMPTY_CODE = "1201"; // 文件内容为空 201
	public final static String FILE_EMPTY_INFO = "The file is empty";

	public final static String FILE_ENCYPTED_ERROR_CODE = "1202"; // 域加密文件"501";
	public final static String FILE_ENCYPTED_ERROR_INFO = "The file is encypted";

	/* 1210-1220：文件内部格式问题 */
	public final static String FILE_SHEET_MISSING_CODE = "1210"; // 找不到符合条件的工作表;
	public final static String FILE_SHEET_MISSING_INFO = "Fail to find the qualified sheet";
	
	public final static String FILE_INVALID_CODE = "1211"; // 无效的文件
	public final static String FILE_INVALID_INFO = "Invalid file format";

	public final static String FILE_KEYWORD_ERROR_CODE = "1211"; // 文件表格中的关键字错误
															// "503";
	public final static String FILE_KEYWORD_ERROR_INFO = "File keywords error";

	public final static String FILE_PARSING_ERROR_CODE = "1220"; // 其他文件解析错误"502";
	public final static String FILE_PARSING_ERROR_INFO = "Invalid file";

	/* 系统异常 */
	public final static String SYSTEM_ERROR_CODE = "1301";// "504";
	public final static String SYSTEM_ERROR_INFO = "系统异常";
}
