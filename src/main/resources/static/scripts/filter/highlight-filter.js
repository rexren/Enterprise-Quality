'use strict';

angular.module('enterprise-quality').filter("highlight",function($sce) {
	return function(e, keywordList) {
		var result = e;
		if (keywordList.length > 0) {
			for(var i = 0; i < keywordList.length; i++){
				var type = keywordList[i];
				var reg = new RegExp(type, 'gi');
				if (type.length != 0 && e.indexOf(type) > -1) {
					//console.log(reg);
					result = result.replace(reg, '<span class=\'highlight\'>'+ type + '</span>');
					//console.log(result);
				}
			}
		}
		return $sce.trustAsHtml(result);
	}
});