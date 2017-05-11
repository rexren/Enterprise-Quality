'use strict';

angular.module('enterprise-quality')
.controller('CccCtrl',['$scope','CCCService',function($scope, ccc){
	$scope.list = ccc.getList({page:1,size:10});
}]);  