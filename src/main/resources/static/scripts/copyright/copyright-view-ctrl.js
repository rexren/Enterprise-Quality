'use strict';

angular.module('enterprise-quality').controller('CopyrightViewCtrl', ['$scope','$location','$http','$q','toastr',
    function($scope, $location, $http, $q, Toastr){
       
	/*initialization*/
    $scope.digest = 
    {
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

    $scope.cpId = $location.search().id;
    
    $scope.modify = function(){
    	$location.url('/copyright/edit?id='+$scope.cpId);
    }
    
    if($scope.cpId){
    	var param = {id: $scope.cpId}; // param : {id, sortby, directioin}
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
        	
        	$scope.digest = {
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
        	$scope.hasCrURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.crUrl)? true : false;
			$scope.hasCdURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.cdUrl)? true : false;
			$scope.hasRgURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.rgUrl)? true : false;
			$scope.hasEpURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.epUrl)? true : false;
		
	    }).error(function(res, status, headers, config){
	        Toastr.error("getListByAjax error: "+status);
	    })
    }
    
    // back
    $scope.back = function() {
    	window.history.back();
    }

}]);