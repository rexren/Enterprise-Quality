'use strict';

angular.module('enterprise-quality').controller('CopyRightSearchModalCtrl',
    function($scope, $modalInstance, keywords){
	// 搜索关键字
	$scope.keywords = keywords;
    
	// 关闭
    $scope.cancel = function() {
    	$modalInstance.dismiss();
    };
    // 确定
    $scope.ok = function() {
    	$modalInstance.close();
    };
});