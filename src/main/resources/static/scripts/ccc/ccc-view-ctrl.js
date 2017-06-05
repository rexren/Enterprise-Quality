'use strict';

angular.module('enterprise-quality').controller('CccViewCtrl', ['$scope','$location','$http','$q','toastr',
    function($scope, $location, $http, $q, Toastr){
	/*initialization*/
    $scope.digest = {
		"docNo":"",
    	"model":"",
    	"productName":"",
    	"awardDate":"",
    	"expiryDate":"",
    	"url":"",
    	"organization":"",
    	"remarks":""
    };
    
    $scope.cccId = $location.search().id;
    if($scope.cccId){
    	var param = {id: $scope.cccId};
        $http.get('/ccc/detail.do',{params:param}).success(function(res){
        	
    		var awardDate = Date.parse(Date(res.data.awardDate));
			awardDate = awardDate>32503651200? new Date(awardDate) : new Date(awardDate*1000);
			
			var expiryDate = Date.parse(Date(res.data.expiryDate));
			expiryDate = expiryDate>32503651200? new Date(expiryDate) : new Date(expiryDate*1000);
			
        	$scope.digest = {
				"docNo":res.data.docNo,
	        	"model":res.data.model,
	        	"productName":res.data.productName,
	        	"awardDate":awardDate,
	        	"expiryDate":expiryDate,
	        	"url":res.data.url,
	        	"organization":res.data.organization,
	        	"remarks":res.data.remarks
        	}
        	$scope.hasURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.url)? true : false;
	
	    }).error(function(res, status, headers, config){
	        Toastr.error("getListByAjax error: "+status);
	    })
    }

    // back
    $scope.back = function(){
        $location.url('/ccc');
    }
	    
}]);