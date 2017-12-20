'use strict';

angular.module('enterprise-quality').controller('CccSearchCtrl', ['$scope','$rootScope','$location','$http', '$modal', '$q','toastr',
    function($scope,$rootScope, $location, $http, $modal, $q, Toastr){
	$scope.authority = $rootScope.user.role;

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
    
    getList();
    function getList(){
    	$scope.isLoading = true;
		$scope.viewList = [];
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
    	    $scope.isLoading = false;
            if(status==401 && res && res.type == -1){
                Toastr.error('登录过期，请刷新重新登录');
                window.location.href='/';
            } else{                 
                Toastr.error("系统错误");
            }
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
			$http.get('/ccc/delete.do',{params:{id:itemId}}).success(function(res){
				if(res.code == 0){
					Toastr.success('删除成功');
					getList($scope.pagination.page, $scope.pagination.size);
				}else{
					if(res.msg){
		        		Toastr.error(res.msg);
		        	}
				}
			}).error(function(res, status, headers, config) {
			    $scope.isLoading = false;
                if(status==401 && res && res.type == -1){
                    Toastr.error('登录过期，请刷新重新登录');
                    window.location.href='/';
                } else{                 
                    Toastr.error("系统错误");
                }
            });
		}, function () {
			// $modalInstance.dismiss 
			$scope.isLoading = false;
		});
    };
    
}]);