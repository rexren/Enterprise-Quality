'use strict';

/**
 * @ngdoc service
 * @name enterprise.common
 * @author langyicong
 * @description
 * # common
 * Service in the enterprise-quality.
 */
angular.module('enterprise-quality')
  .service('common', ['$q','$http','toastr',
    function ($q,$http,Toastr) {
    	var service = {
    		retCodeHandler: function(retCode){
	        	switch (retCode) {
	                case '1001':
	                	Toastr.error('表单有误');
	                    break;
	                case '1201':
	                	Toastr.error('文件内容为空');
	                    break;
	                case '1202':
	                	Toastr.error('文件被加密，请上传未加密的文件');
	                    break;
	                case '1210':
	                	Toastr.error('找不到符合条件的工作表');
	                    break;
	                case '1211':
	                	Toastr.error('表格关键字有误');
	                    break;
	                case '1220':
	                	Toastr.error('文件解析失败');
	                    break;
	                default:
	                	Toastr.error('后台繁忙！');
	                    break;
	            }
    		}
    	};
    	return service;
  }]);