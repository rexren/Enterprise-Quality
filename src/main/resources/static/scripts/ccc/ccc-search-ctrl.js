'use strict';

angular.module('enterprise-quality').controller('CccSearchCtrl', ['$scope','$rootScope','$location','$http','$q','toastr',
    function($scope,$rootScope, $location, $http, $q, Toastr){
	$scope.authority = $rootScope.user.roles?$rootScope.user.roles[0]: null;

    $scope.pagination = {
        page: 1,
        size: 20,
        totalElements: 0
    };
    $scope.viewList = [];
    $scope.onPageChange = function(page){
        $scope.pagination.page = page;
        $scope.viewList = $scope.list?$scope.list.slice((page-1) * $scope.pagination.size, page * $scope.pagination.size):[];
    };

    $scope.searchInput = {
    	'field': $location.search().f,
    	'keyword':$location.search().kw
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
    $scope.isLoading = true;
    $http.get('/ccc/search.do', {
        params: $scope.searchInput
    }).success(function(res) {
        if (res.code == 0) {
        	$scope.list = res.listContent? res.listContent.list : null;
            $scope.pagination.totalElements = res.listContent?res.listContent.totalElements:null;
            if($scope.list && $scope.list.length > 0){
            	for (var i = 0; i < $scope.list.length; i++) {
            		$scope.list[i].hasURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].url)? true : false;
       			 
            	}
            	$scope.viewList = $scope.list.slice(($scope.pagination.page-1)* $scope.pagination.size, ($scope.pagination.page)* $scope.pagination.size);
            }
        } else {
        	if(res.msg){
        		Toastr.error(res.msg);
        	} else {
        		Toastr.error('登录过期，请重新登录');
        	}
        }
        $scope.isLoading = false;
    }).error(function(res, status) {
        Toastr.error("网络错误");
        $scope.isLoading = false;
    })
    
}]);