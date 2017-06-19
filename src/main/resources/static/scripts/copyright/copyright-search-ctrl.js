'use strict';

angular.module('enterprise-quality').controller('CopyrightSearchCtrl', ['$scope','$rootScope','$location','$http','$q','toastr',
    function($scope, $rootScope, $location, $http, $q, Toastr){
	$scope.authority = $rootScope.user.roles?$rootScope.user.roles[0]: null;

    $scope.pagination = {
        page: 1,
        size: 20,
        totalElements: 0
    };

    $scope.onPageChange = function(page){
        $scope.pagination.page = page;
        getList($scope.pagination.page, $scope.pagination.size);
    };

    $scope.searchInput = {
    	"field": $location.search().f,
    	"keyword":$location.search().kw
    };
    
    $scope.keywordsList = $scope.searchInput.keyword?$scope.searchInput.keyword.split(/\s+/):[];
    
    switch ($scope.searchInput.field) {
    	case '1':  $scope.searchField = '软件名称'; break;
    	case '2':  $scope.searchField = '简称'; break;
    	case '3':  $scope.searchField = '软件型号'; break;
    	case '4':  $scope.searchField = '软著登记号'; break;
    	case '5':  $scope.searchField = '软件产品登记证书'; break;
    	case '6':  $scope.searchField = '软件测评报告号'; break;
    	case '7':  $scope.searchField = '类别界定报告号'; break;
    	default: $scope.searchField = '全部'; break;
    }
    
    $http.get('/copyright/search.do', {
        params: $scope.searchInput
    }).success(function(res) {
        if (res.code == 0) {
        	$scope.list = res.listContent? res.listContent.list : null;
            $scope.pagination.totalElements = res.listContent?res.listContent.totalElements:null;
    		if($scope.list && $scope.list.length > 0){
    			 for(var i=0; i<$scope.list.length; i++){
        			$scope.list[i].hasCrURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].crUrl)? true : false;
        			$scope.list[i].hasCdURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].cdUrl)? true : false;
        			$scope.list[i].hasRgURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].rgUrl)? true : false;
        			$scope.list[i].hasEpURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].epUrl)? true : false;
    			 }
    		}
        } else {
        	if(res.msg){
        		Toastr.error(res.msg);
        	} else {
        		Toastr.error('登录过期，请重新登录');
        	}
        }
    }).error(function(res, status) {
        Toastr.error("网络错误");
    })

}]);