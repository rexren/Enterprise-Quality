'use strict';

angular.module('enterprise-quality').controller('CccEditCtrl',['$scope','$rootScope','$location','$http','$modal','$q','toastr','FileUploadService','common',
    function($scope, $rootScope, $location, $http, $modal, $q, Toastr, FileUploadService, Common){
	$scope.authority = $rootScope.user.roles?$rootScope.user.roles[0]: null;
	if($scope.authority!='ROLE_ADMIN'){
		$location.path('/unauthorized');
	}
	$scope.isLoading = false;
    $scope.formData = {
    	"docNo":"",
    	"model":"",
    	"productName":"",
    	"awardDate":"",
    	"expiryDate":"",
    	"url":"",
    	"organization":"",
    	"remarks":""
    };
    var targetUrl = '/ccc/save.do';
    $scope.target = $location.search().target;
    var urlId = $location.search().id;
    if(urlId){
    	var param = {id: urlId}; 
    	$scope.isLoading = true;
    	$http.get('/ccc/detail.do',{params:param}).success(function(res) {
    		if(res.code == 0){
    			var awardDate =  res.data.awardDate?new Date(Number(res.data.awardDate)):null;
    			var expiryDate =  res.data.expiryDate?new Date(Number(res.data.expiryDate)):null;
    			
    			$scope.formData = {
					"docNo":res.data.docNo,
					"model":res.data.model,
					"productName":res.data.productName,
					"awardDate":awardDate,
					"expiryDate":expiryDate,
					"url":res.data.url,
					"organization":res.data.organization,
					"remarks":res.data.remarks
    			}
    		}else{
    			if(res.msg){
	        		Toastr.error(res.msg);
	        	} else {
	        		Toastr.error('登录过期，请刷新重新登录');
	        		window.location.href='/login';
	        	}
    		}
    		$scope.isLoading = false;
		}).error(function(res, status, headers, config){
	        Toastr.error("Ajax error: "+status);
	        $scope.isLoading = false;
	    })
	    targetUrl = '/ccc/update.do';
    }

    $scope.submit = function () {
    	if ($scope.formData.docNo == '') {
        	Toastr.error('请输入文件编号');
    	}else if($scope.formData.model == '') {
    		Toastr.error('请输入产品型号');
    	} else if ($scope.formData.productName == '') {
    		Toastr.error('请输入产品名称');
        }else {
            
            var params = angular.extend({},$scope.formData);
    		params['awardDate'] = Date.parse($scope.formData.awardDate);
    		params['expiryDate'] = Date.parse($scope.formData.expiryDate);
    		if (urlId) {
    			params = angular.extend(params, {'id': urlId});
            }
    		$scope.isLoading = true;
    		$http({
    			method  : 'POST',
    			url     : targetUrl,
    			data    : $.param(params),  // pass in data as strings
    			headers : { 
    				'Content-Type': 'application/x-www-form-urlencoded' 
    			}
    		}).success(function(res) {
    			if (res.code == 0) {
    				Toastr.success('保存成功');
                	$location.url('/ccc');
    			}else{
    				if(res.msg){
    	        		Toastr.error(res.msg);
    	        	} else {
    	        		Toastr.error('登录过期，请刷新重新登录');
    	        		window.location.href='/login';
    	        	}
            	}
    			$scope.isLoading = false;
    		}).error(function(res, status, headers, config) {
            	Toastr.error("AjaxError: "+ status);
            	$scope.isLoading = false;
            });
        }
    };
    
    // back
    $scope.back = function(){
    	window.history.back();
    }    
    // close
    $scope.close = function() {
    	window.close();
    }

}]);