'use strict';

angular.module('enterprise-quality').controller('CopyrightViewCtrl', ['$scope', '$location', '$http', '$q', 'toastr',
    function($scope, $location, $http, $q, Toastr) {

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
	                var crDate = res.data.crDate ? new Date(Number(res.data.crDate)) : null;
	                var rgDate = res.data.rgDate ? new Date(Number(res.data.rgDate)) : null;
	                var rgExpiryDate = res.data.rgExpiryDate ? new Date(Number(res.data.rgExpiryDate)) : null;
	                var epDate = res.data.epDate ? new Date(Number(res.data.epDate)) : null;
	                var cdDate = res.data.crDate ? new Date(Number(res.data.cdDate)) : null;
	
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
	                    'rgExpiryDate': rgExpiryDate,
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