'use strict';

angular.module('enterprise-quality')
.controller('CccCtrl',['$scope','$location','$http',function($scope,$location,$http){
	$scope.pagination = {
		page: 1,
		size: 10,
		totalElements: 0
	};
	getList($scope.pagination.page, $scope.pagination.size);
	
	$scope.onPageChange = function(page){
		$scope.pagination.page = page;
		getList($scope.pagination.page, $scope.pagination.size);
	}

    function getList(page, size) {
        var param = {pageNum:page,pageSize:size};
        $http.get('/ccc/list.action',{params:param}).success(function(res){
        	console.log(res);
            $scope.list = res.content;
            $scope.pagination.totalElements = res.totalElements;
        }).error(function(res, status, headers, config){
            alert("getListByAjax error: "+status);
        })
    }
}]);  