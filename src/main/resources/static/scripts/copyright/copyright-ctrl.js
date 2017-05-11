'use strict';

angular.module('enterprise-quality')
.controller('CopyrightCtrl',['$scope','CopyrightService',function($scope,copyright){
	$scope.name = "ccoooopy";
	$scope.list = copyright.getList({page:2,size:10});
}]);  