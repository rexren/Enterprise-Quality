'use strict';

angular.module('enterprise-quality')
    .controller('InspectionsEditCtrl', ['$scope','$location',
        function($scope, $location){
            console.log('InspectionsEditCtrl');
            console.log($location.search().id);
            $scope.formData = 
            {
                    'model': '',
                    'name': '',
                    'version': '',
                    'testType': '',
                    'company': '',
                    'basis': '',
                    'awardDate': '',
                    'docNo': '',
                    'certUrl': '',
                    'organization': '',
                    'remarks': '',
                    'operator': ''
            };
            
            $scope.submit = function () {
                console.log($scope.formData);
            };
            // back
            $scope.back = function(){
                $location.url('/inspections');
            }

        }]);