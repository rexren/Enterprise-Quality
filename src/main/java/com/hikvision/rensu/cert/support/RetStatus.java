package com.hikvision.rensu.cert.support;

public enum RetStatus{
		SUCCESS_CODE("0","Success"), 
		FORM_VALIDATE_ERROR_CODE("1001","Form data is invalid");
		private String retCode;
		private  String retInfo;
		RetStatus(String retCode, String retInfo){
			this.retCode = retCode;
			this.retInfo = retInfo;
		}
		public String getRetCode() {
			return retCode;
		}
		public String getRetInfo() {
			return retInfo;
		}
	}
