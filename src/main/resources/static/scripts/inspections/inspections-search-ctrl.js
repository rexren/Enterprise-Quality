'use strict';

angular.module('enterprise-quality').controller('InspectionsSearchCtrl', ['$scope','$rootScope','$location','$http','$q','toastr',
    function($scope, $rootScope, $location, $http, $q, Toastr){
		$scope.authority = $rootScope.user.roles?$rootScope.user.roles[0]: null;
		$scope.isLoading = true;
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
        	'field': $location.search().f,
        	'keyword':$location.search().kw,
        	'contentKeyword':$location.search().c
        };
        $scope.tkeywordsList=$scope.searchInput.keyword?$scope.searchInput.keyword.split(/\s+/):[];
        $scope.ckeywordsList=$scope.searchInput.contentKeyword?$scope.searchInput.contentKeyword.split(/\s+/):[];
        
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
	        	$scope.list = res.listContent? res.listContent.list:null;
	            $scope.pagination.totalElements = res.listContent?res.listContent.totalElements:null;
	            
	            if($scope.list && $scope.list.length > 0){
	            	for (var i = 0; i < $scope.list.length; i++) {
	            		/*截取500个字符*/
	            		$scope.list[i].casesCut = $scope.list[i].cases.substring(0,500);
	            		if($scope.list[i].cases.length>500){
	            			$scope.list[i].casesCut +='...';
	            		}
	            		/* 链接判断 */
	            		$scope.list[i].typeInspection.hasURL = /.*(http|https).*/.test($scope.list[i].typeInspection.certUrl)? true : false;  
	            	}
	            }
	        } else {
	        	if(res.msg){
	        		Toastr.error(res.msg);
	        	} else {
	        		Toastr.error('登录过期，请刷新重新登录');
	        		window.location.href='/login';
	        	}
	        }
	        $scope.isLoading = false;
	    }).error(function(res, status) {
	        Toastr.error("getListByAjax error: " + status);
	        $scope.isLoading = false;
	    })
        
    }]);