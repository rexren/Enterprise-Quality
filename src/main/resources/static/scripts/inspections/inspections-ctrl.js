'use strict';

angular.module('enterprise-quality')
    .controller('InspectionsCtrl', ['$scope','$location','InpectionService',
    	function($scope, $location, inspection){
        $scope.list = inspection.getList({page:1,size:10});
        $scope.addInspection = function(){
        	$location.url('/inspections/edit');
        }
    }]);