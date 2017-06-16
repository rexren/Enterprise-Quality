'use strict';

angular.module('enterprise-quality').controller('CccSearchCtrl', ['$scope','$rootScope','$location','$http','$q','toastr',
    function($scope,$rootScope, $location, $http, $q, Toastr){
		$scope.authority = $rootScope.user.authorities[0]?$rootScope.user.authorities[0].authority: null;
    
        $scope.pagination = {
            page: 1,
            size: 20,
            totalElements: 0
        };
        
        $scope.onPageChange = function(page){
            $scope.pagination.page = page;
            getList($scope.pagination.page, $scope.pagination.size);
        };

		//TODO 关键词的高亮显示
        $scope.searchInput = {
        	'field': $location.search().f,
        	'keyword':$location.search().kw
        };
        
        switch ($scope.searchInput.field) {
        	case '':  $scope.searchField = '全部'; break;
        	case '1':  $scope.searchField = '文件编号'; break;
        	case '2':  $scope.searchField = '产品型号'; break;
        	case '3':  $scope.searchField = '产品名称'; break;
        	case '4':  $scope.searchField = '备注'; break;
        	default: $scope.searchField = '全部'; break;
        }
        
        $http.get('/ccc/search.do', {
            params: $scope.searchInput
        }).success(function(res) {
            if (res.code == 0) {
            	$scope.list = res.listContent?res.listContent.list:null;
	            $scope.pagination.totalElements = res.listContent?res.listContent.totalElements:null;
	            if($scope.list && $scope.list.length > 0){
	            	for (var i = 0; i < $scope.list.length; i++) {
	            		$scope.list[i].hasURL = /.*(http|https).*/.test($scope.list[i].url) ? true : false;
	            	}
                }
            } else {
                Toastr.error(res.msg);
            }
        }).error(function(res, status) {
            Toastr.error("getListByAjax error: " + status);
        })
        
    }]);