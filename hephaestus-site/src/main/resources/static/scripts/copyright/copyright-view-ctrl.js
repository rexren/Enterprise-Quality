'use strict';

angular.module('enterprise-quality').controller('CopyrightViewCtrl', ['$scope', '$rootScope', '$location', '$http', '$q', 'toastr',
    function($scope, $rootScope, $location, $http, $q, Toastr) {
	$scope.authority = $rootScope.user.roles?$rootScope.user.roles[0]: null;
	$scope.isLoading = false;
    /*initialization*/
    $scope.digest = {
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

    var crId = $location.search().id;
    
    $scope.searchInput = {
        "field": $location.search().f,
        "keyword":$location.search().kw
    };
    $scope.keywordsList = $scope.searchInput.keyword?$scope.searchInput.keyword.split(/\s+/):[];
    
    switch ($scope.searchInput.field) {
		case '1':  $scope.searchField = '软件名称'; break;
		case '2':  $scope.searchField = '简称'; break;
		case '3':  $scope.searchField = '软件型号'; break;
		case '4':  $scope.searchField = '软著登记号'; break;
		case '5':  $scope.searchField = '软件产品登记证书'; break;
		case '6':  $scope.searchField = '软件测评报告号'; break;
		case '7':  $scope.searchField = '类别界定报告号'; break;
		default: $scope.searchField = '全部'; break;
    }

    if (crId) {
        var param = {
            id: crId
        };
        $scope.isLoading = true;
        $http.get('/copyright/detail.do', {
            params: param
        }).success(function(res) {
        	if(res.code == 0){
                angular.extend($scope.digest, res.data);
                $scope.hasCrURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.crUrl) ? true : false;
                $scope.hasCdURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.cdUrl) ? true : false;
                $scope.hasRgURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.rgUrl) ? true : false;
                $scope.hasEpURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.digest.epUrl) ? true : false;
        	} else {
        		if(res.msg){
	        		Toastr.error(res.msg);
	        	} else {
	        		Toastr.error('登录过期，请刷新重新登录');
	        		window.location.href='/login';
	        	}
        	}
        	$scope.isLoading = false;
        }).error(function(res, status, headers, config) {
            Toastr.error("getListByAjax error: " + status);
            $scope.isLoading = false;
        })
    }
    
    // edit
    $scope.edit = function() {
        $location.url('/copyright/edit?id='+crId);
    }
    
    // back
    $scope.back = function() {
        window.history.back();
    }
    
    //close
    $scope.close = function(){
    	window.close();
    }
}]);