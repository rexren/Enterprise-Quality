'use strict';

angular.module('enterprise-quality').controller('CopyrightEditCtrl', 
	function($scope, $location, $http, $q, FileUploadService){
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
            
    	    $scope.fileName = '';
    	    
            var urlId = $location.search().id;
            if(urlId){
            	var param = {id: urlId};
    	        $http.get('/copyright/detail.do',{params:param}).success(function(res){
    	        	console.log('success');
    	        	console.log(res);
    	        	
    	        	var crDate = Date.parse(Date(res.crDate));
    	        	crDate = crDate>32503651200? new Date(crDate) : new Date(crDate*1000);

    	        	var rgDate = Date.parse(Date(res.rgDate));
    	        	rgDate = rgDate>32503651200? new Date(rgDate) : new Date(rgDate*1000);

    	        	var rgExpiryDate = Date.parse(Date(res.rgExpiryDate));
    	        	rgExpiryDate = rgExpiryDate>32503651200? new Date(rgExpiryDate) : new Date(rgExpiryDate*1000);

    	        	var epDate = Date.parse(Date(res.epDate));
    	        	epDate = epDate>32503651200? new Date(epDate) : new Date(epDate*1000);

    	        	var cdDate = Date.parse(Date(res.cdDate));
    	        	cdDate = cdDate>32503651200? new Date(cdDate) : new Date(cdDate*1000);

    	        	$scope.formData = {
	                    'softwareName': res.softwareName,
	                    'abbreviation': res.abbreviation,
	                    'crNo': res.crNo,
	                    'crDate': crDate,
	                    'crUrl': res.crUrl,
	                    'crOrganization': res.crOrganization,
	                    'crSoftwareType': res.crSoftwareType,
	                    'rgNo': res.rgNo,
	                    'rgDate': rgDate,
	                    'rgExpiryDate':rgExpiryDate,
	                    'rgUrl': res.rgUrl,
	                    'rgOrganization': res.rgOrganization,
	                    'epNo': res.epNo,
	                    'epDate': epDate,
	                    'epUrl': res.epUrl,
	                    'epOrganization': res.epOrganization,
	                    'cdNo': res.cdNo,
	                    'cdDate': cdDate,
	                    'cdUrl': res.cdUrl,
	                    'cdOrganization': res.cdOrganization,
	                    'model': res.model,
	                    'charge': res.charge
    	        	}
    		    }).error(function(res, status, headers, config){
    		        alert("getListByAjax error: "+status);
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
            $scope.back = function(){
                $location.url('/copyright');
            }

        });