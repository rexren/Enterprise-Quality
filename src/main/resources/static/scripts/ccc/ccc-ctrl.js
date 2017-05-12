'use strict';

angular.module('enterprise-quality')
.controller('CccCtrl',['$scope','$location','CCCService',function($scope,$location,ccc){
	$scope.list = ccc.getList({page:1,size:10});
	$scope.edit = function(){
		$location.url('/ccc/edit');
	}
}]);  