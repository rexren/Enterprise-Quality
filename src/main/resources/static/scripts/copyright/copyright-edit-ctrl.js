'use strict';

angular.module('enterprise-quality')
    .controller('CopyrightEditCtrl', ['$scope','$location',
        function($scope, $location){

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
            $scope.submit = function () {
                console.log($scope.formData);
            };
            // back
            $scope.back = function(){
                $location.url('/copyright');
            }

        }]);