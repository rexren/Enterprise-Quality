'use strict';

angular.module('enterprise-quality').controller('CopyrightEditCtrl', ['$scope','$rootScope','$location','$http','$modal','$q','toastr','FileUploadService','common',
    function($scope, $rootScope, $location, $http, $modal, $q, Toastr, FileUploadService, Common){
	$scope.authority = $rootScope.user.role;
	
	if($scope.authority!='ROLE_ADMIN'){
		$location.path('/unauthorized');
	}
	$scope.isLoading = false;
    $scope.formData = {
        'softwareName': '',
        'abbreviation': '',
        'crNo': '',
        'crDate': '',
        'crUrl': '',
        'crOrganization': '',
        'crSoftwareType': '',
        'rgNo': '',
        'rgDate': '',
        'rgExpiryDate': '',
        'rgUrl': '',
        'rgOrganization': '',
        'epNo': '',
        'epDate': '',
        'epUrl': '',
        'epOrganization': '',
        'cdNo': '',
        'cdDate': '',
        'cdUrl': '',
        'cdOrganization': '',
        'model': '',
        'charge': ''
    };
    var targetUrl = '/copyright/save.do';

    $scope.target = $location.search().target;
    
    var urlId = $location.search().id;
    if(urlId){
    	var param = {id: urlId}; 
    	$scope.isLoading = true;
    	$http.get('/copyright/detail.do',{params:param}).success(function(res){
        	if(res.code == 0){
	        	var crDate =  res.data.crDate?new Date(Number(res.data.crDate)):null;
	        	var rgDate =  res.data.rgDate?new Date(Number(res.data.rgDate)):null;
	        	var rgExpiryDate =  res.data.rgExpiryDate?new Date(Number(res.data.rgExpiryDate)):null;
	        	var epDate =  res.data.epDate?new Date(Number(res.data.epDate)):null;
	        	var cdDate =  res.data.cdDate?new Date(Number(res.data.cdDate)):null;
   	
	        	$scope.formData = {
                    'softwareName': res.data.softwareName,
                    'abbreviation': res.data.abbreviation,
                    'crNo': res.data.crNo,
                    'crDate': crDate,
                    'crUrl': res.data.crUrl,
                    'crOrganization': res.data.crOrganization,
                    'crSoftwareType': res.data.crSoftwareType,
                    'rgNo': res.data.rgNo,
                    'rgDate': rgDate,
                    'rgExpiryDate':rgExpiryDate,
                    'rgUrl': res.data.rgUrl,
                    'rgOrganization': res.data.rgOrganization,
                    'epNo': res.data.epNo,
                    'epDate': epDate,
                    'epUrl': res.data.epUrl,
                    'epOrganization': res.data.epOrganization,
                    'cdNo': res.data.cdNo,
                    'cdDate': cdDate,
                    'cdUrl': res.data.cdUrl,
                    'cdOrganization': res.data.cdOrganization,
                    'model': res.data.model,
                    'charge': res.data.charge
	        	}
        	} else{
        		if(res.msg){
	        		Toastr.error(res.msg);
	        	} else {
	        		Toastr.error('登录过期，请刷新重新登录');
	        		window.location.href='/login';
	        	}
        	}
        	$scope.isLoading = false;
	    }).error(function(res, status, headers, config){
	        Toastr.error("getListByAjax error: "+status);
	        $scope.isLoading = false;
	    })
	    targetUrl = '/copyright/update.do';
    }

    $scope.submit = function () {
    	if($scope.formData.softwareName==''){
    		Toastr.error("请输入软件名称");
    	} else{
    		$scope.isLoading = true;
    		var params = angular.extend({},$scope.formData);
    		params['crDate'] = Date.parse($scope.formData.crDate);
    		params['rgDate'] = Date.parse($scope.formData.rgDate);
    		params['rgExpiryDate'] = Date.parse($scope.formData.rgExpiryDate);
    		params['epDate'] = Date.parse($scope.formData.epDate);
    		params['cdDate'] = Date.parse($scope.formData.cdDate);
    		if (urlId) {
    			params = angular.extend(params, {'id': urlId});
            }
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
                	$location.url('/copyright');
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
    $scope.back = function() {
    	window.history.back();
    }

    // close
    $scope.close = function() {
    	window.close();
    }
    
}]);