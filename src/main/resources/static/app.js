'use strict';

angular.module('enterprise-quality',['ngRoute'])
.config(['$routeProvider', function($routeProvider){
    $routeProvider
    	.when('/',{
    		template:'这是首页页面{{nnn}}',
    		controller:'homeCtrl'
    	})
    	.when('/inspections',{
    		templateUrl: 'html/inspections.html',
    		controller: 'inspectionsCtrl'
    	})
    	.when('/copyright',{
    		templateUrl: 'html/copyright.html',
    		controller: 'copyrightCtrl'
    	})
    	.when('/ccc',{
    		templateUrl: 'html/ccc.html',
    		controller: 'cccCtrl'
    	})
    	.otherwise({
            redirectTo: '/inspections'
        })
    }
])
.controller('homeCtrl', ['$scope',function($scope) {
	$scope.nnn='HOME';
	console.log('homeCtrl scope');
}])
.controller('inspectionsCtrl', ['$scope',function($scope){
    console.log('inspectionsCtrl scope');
    $scope.name = "hhhhh";
}]);
