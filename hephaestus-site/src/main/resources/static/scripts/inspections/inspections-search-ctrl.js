'use strict';

angular.module('enterprise-quality').controller('InspectionsSearchCtrl', ['$scope','$rootScope','$location','$http','$modal','$q','toastr',
    function($scope, $rootScope, $location, $http, $modal, $q, Toastr){
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
        getList();
        
        function getList() {
        	$scope.viewList = [];
        	$scope.isLoading = true;
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
		            	$scope.viewList = $scope.list.slice(($scope.pagination.page-1)* $scope.pagination.size, ($scope.pagination.page)* $scope.pagination.size);
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
        }
        
        /**
		 * 删除条目
		 */
		$scope.delete = function(itemId) {
			// 打开弹窗
			var modalInstance = $modal.open({
		        animation: true,
		        templateUrl: '/html/modal-delete.html',
		        controller: 'deleteModalCtrl',
		        size: 'sm',
		        resolve: {
		        	data: function () {
		        		return itemId;
		        	}
		        }
		    });
			// 弹窗在关闭的时候执行的
			modalInstance.result.then(function () {
				// $modalInstance.close 
				$scope.isLoading = false;
				$http.get('/inspections/delete.do',{params:{id:itemId}}).success(function(res){
					if(res.code == 0){
						Toastr.success('删除成功');
						getList();
					}else{
						if(res.msg){
			        		Toastr.error(res.msg);
			        	} else {
			        		Toastr.error('登录过期，请重新登录');
			        	}
					}
				}).error(function(res, status, headers, config) {
	            	Toastr.error("AjaxError: "+ status);
	            	$scope.isLoading = false;
	            });
			}, function () {
				// $modalInstance.dismiss 
				$scope.isLoading = false;
			});
	    };

    }]);