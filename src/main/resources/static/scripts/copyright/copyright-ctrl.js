'use strict';

angular.module('enterprise-quality')
.controller('CopyrightCtrl',['$scope','$location','CopyrightService',
	function($scope,$location,copyright){
	$scope.list = copyright.getList({page:2,size:10});
    $scope.editCopyright = function(id){
        $location.url('/copyright/edit');
    }
}]);  