'use strict';

angular.module('enterprise-quality').controller('CccViewCtrl', ['$scope','$rootScope','$location','$http','$q','toastr',
    function($scope, $rootScope, $location, $http, $q, Toastr){
	/*initialization*/
	$scope.authority = $rootScope.user.role;
	$scope.isLoading = false;
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
    
    $scope.searchInput = {
        "field": $location.search().f,
        "keyword":$location.search().kw
    };
    $scope.keywordsList = $scope.searchInput.keyword?$scope.searchInput.keyword.split(/\s+/):[];
    
    switch ($scope.searchInput.field) {
		case '':  $scope.searchField = '全部'; break;
		case '1':  $scope.searchField = '文件编号'; break;
		case '2':  $scope.searchField = '产品型号'; break;
		case '3':  $scope.searchField = '产品名称'; break;
		case '4':  $scope.searchField = '备注'; break;
		default: $scope.searchField = '全部'; break;
	}
    
    if(cccId){
    	var param = {id: cccId};
    	$scope.isLoading = true;
        $http.get('/ccc/detail.do',{params:param}).success(function(res){
        	if(res.code==0){
        		angular.extend($scope.digest, res.data);
        		$scope.hasURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.url)? true : false;
        	}else{
        		if(res.msg){
	        		Toastr.error(res.msg);
	        	}
        	}
        	$scope.isLoading = false;
	    }).error(function(res, status, headers, config){
	        $scope.isLoading = false;
            if(status==401 && res && res.type == -1){
                Toastr.error('登录过期，请刷新重新登录');
                window.location.href='/';
            } else{                 
                Toastr.error("系统错误");
            }
	    })
    }

    // edit
    $scope.edit = function() {
        $location.url('/ccc/edit?id='+cccId);
    }
    
    // back
    $scope.back = function(){
    	window.history.back();
    } 
    
    //close
    $scope.close = function(){
    	window.close();
    }
	    
}]);