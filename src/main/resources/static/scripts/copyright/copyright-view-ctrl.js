'use strict';

angular.module('enterprise-quality').controller('CopyrightViewCtrl', ['$scope', '$rootScope', '$location', '$http', '$q', 'toastr',
    function($scope, $rootScope, $location, $http, $q, Toastr) {
		$scope.authority = $rootScope.user.authorities[0].authority;
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

        $scope.modify = function() {
            $location.url('/copyright/edit?id=' + crId);
        }

        if (crId) {
            var param = {
                id: crId
            };
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
            		Toastr.error(res.msg);
            	}
            }).error(function(res, status, headers, config) {
                Toastr.error("getListByAjax error: " + status);
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

    }
]);