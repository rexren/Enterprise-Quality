'use strict';

angular.module('enterprise-quality').controller('CccViewCtrl', ['$scope','$rootScope','$location','$http','$q','toastr',
    function($scope, $rootScope, $location, $http, $q, Toastr){
	/*initialization*/
	$scope.authority = $rootScope.user.authorities[0].authority;
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
    
    var cccId = $location.search().id;
    if(cccId){
    	var param = {id: cccId};
        $http.get('/ccc/detail.do',{params:param}).success(function(res){
        	var awardDate =  res.data.awardDate?new Date(Number(res.data.awardDate)):null;
    		var expiryDate =  res.data.expiryDate?new Date(Number(res.data.expiryDate)):null;

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

    // edit
    $scope.edit = function() {
        $location.url('/ccc/edit?id='+cccId);
    }
    
    // back
    $scope.back = function(){
        $location.url('/ccc');
    }
	    
}]);