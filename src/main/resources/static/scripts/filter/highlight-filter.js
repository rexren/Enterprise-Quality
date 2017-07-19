'use strict';

angular.module('enterprise-quality').filter("highlight",function($sce) {
	return function(e, keywordList) {
		var result = e;
		if (keywordList && keywordList.length > 0) {
			for(var i = 0; i < keywordList.length; i++){
				var kw = keywordList[i];
				var reg = new RegExp(kw, 'gi'); 
				//TODO 匹配大小写字符，高亮时大小写保持不变
				if (kw.length != 0 && e.indexOf(kw) > -1) {
					result = result.replace(reg, '<span class=\'highlight\'>'+ kw + '</span>');
				}
			}
		}
		return $sce.trustAsHtml(result);
	}
});