'use strict';

angular.module('enterprise-quality').controller('CopyrightEditCtrl', ['$scope','$location','$http','$modal','$q','toastr','FileUploadService','common',
    function($scope, $location, $http, $modal, $q, Toastr, FileUploadService, Common){
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
            var urlId = $location.search().id;
            if(urlId){
            	var param = {id: urlId}; // param : {id, sortby, directioin}
    	        $http.get('/copyright/detail.do',{params:param}).success(function(res){
    	        	
    	        	var crDate = Date.parse(Date(res.data.crDate));
    	        	crDate = crDate>32503651200? new Date(crDate) : new Date(crDate*1000);

    	        	var rgDate = Date.parse(Date(res.data.rgDate));
    	        	rgDate = rgDate>32503651200? new Date(rgDate) : new Date(rgDate*1000);

    	        	var rgExpiryDate = Date.parse(Date(res.data.rgExpiryDate));
    	        	rgExpiryDate = rgExpiryDate>32503651200? new Date(rgExpiryDate) : new Date(rgExpiryDate*1000);

    	        	var epDate = Date.parse(Date(res.data.epDate));
    	        	epDate = epDate>32503651200? new Date(epDate) : new Date(epDate*1000);

    	        	var cdDate = Date.parse(Date(res.data.cdDate));
    	        	cdDate = cdDate>32503651200? new Date(cdDate) : new Date(cdDate*1000);

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
    		    }).error(function(res, status, headers, config){
    		        Toastr.error("getListByAjax error: "+status);
    		    })
            }

            $scope.submit = function () {
            	//TODO 发送前数据整理
            	 $http({
            	     method  : 'POST',
            	     url     : '/copyright/save.do',
            	     data    : $.param($scope.formData),  // pass in data as strings
            	     headers : { 
            	    	 'Content-Type': 'application/x-www-form-urlencoded' 
            	     }
            	 }).success(function(res) {
            	     console.log(res);
            	});
            };

            // back
            $scope.back = function() {
            	window.history.back();
            }

        }]);