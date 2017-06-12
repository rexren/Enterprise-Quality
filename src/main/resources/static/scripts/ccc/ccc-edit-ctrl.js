'use strict';

angular.module('enterprise-quality').controller('CccEditCtrl',['$scope','$rootScope','$location','$http','$modal','$q','toastr','FileUploadService','common',
    function($scope, $rootScope, $location, $http, $modal, $q, Toastr, FileUploadService, Common){
		$scope.authority = $rootScope.user.authorities[0].authority;
    	if($scope.authority!='ROLE_ADMIN'){
			$location.path('/unauthorized');
		}
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
        
        var urlId = $location.search().id;
        if(urlId){
        	var param = {id: urlId}; 
        	$http.get('/ccc/detail.do',{params:param}).success(function(res) {	
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
        		console.log($scope.formData);
			}).error(function(res, status, headers, config){
		        Toastr.error("getListByAjax error: "+status);
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
                		Toastr.error(res.msg);
                	}
        		}).error(function(res, status, headers, config) {
                	Toastr.error("AjaxError: "+ status);
                });
            }
        };
        
        // back
        $scope.back = function(){
        	window.history.back();
        }

    }]);