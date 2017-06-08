'use strict';

angular.module('enterprise-quality').controller('InspectionsSearchCtrl', ['$scope','$location','$http','$q','toastr',
    function($scope, $location, $http, $q, Toastr){
        $scope.pagination = {
            page: 1,
            size: 20,
            totalElements: 0
        };

        $scope.onPageChange = function(page){
            $scope.pagination.page = page;
            getList($scope.pagination.page, $scope.pagination.size);
        };

        //todo url中读取searchInput
        $scope.searchInput = {
        	'field': $location.search().f,
        	'keyword':$location.search().kw,
        	'contentKeyword':$location.search().c
        };
        
        switch ($scope.searchInput.field) {
	    	case '1':  $scope.searchField = '产品型号'; break;
	    	case '2':  $scope.searchField = '软件名称'; break;
	    	case '3':  $scope.searchField = '测试类别'; break;
	    	case '4':  $scope.searchField = '测试依据'; break;
	    	case '5':  $scope.searchField = '文件编号'; break;
	    	case '6':  $scope.searchField = '认证机构'; break;
	    	case '7':  $scope.searchField = '备注'; break;
	    	default: $scope.searchField = '全部'; break;
        }
        
        $http.get('/inspections/search.do', {
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