'use strict';

angular.module('enterprise-quality',[
	'ngAnimate',
    'ngRoute',
    'ui.bootstrap',
    'toastr',
    'ngSanitize'
    ])
.config(['$routeProvider', function($routeProvider){
    $routeProvider
    	.when('/',{
    		redirectTo: '/updatelogs'
    	})
    	.when('/updatelogs',{
    		templateUrl:'html/update-logs.html',
    		controller:'UpdateLogsCtrl'
    	})
    	.when('/inspections',{
    		templateUrl: 'html/inspections.html',
    		controller: 'InspectionsCtrl'
    	})
    	.when('/inspections/edit',{
    		templateUrl: 'html/inspection-edit.html',
    		controller: 'InspectionsEditCtrl'
    	})
    	.when('/inspections/view',{
    		templateUrl: 'html/inspection-view.html',
    		controller: 'InspectionsViewCtrl'
    	})
    	.when('/inspections/search',{
    		templateUrl: 'html/inspection-search.html',
    		controller: 'InspectionsSearchCtrl'
    	})
        .when('/copyright',{
            templateUrl: 'html/copyright.html',
            controller: 'CopyrightCtrl'
        })
        .when('/copyright/view',{
            templateUrl: 'html/copyright-view.html',
            controller: 'CopyrightViewCtrl'
        })
        .when('/copyright/edit',{
            templateUrl: 'html/copyright-edit.html',
            controller: 'CopyrightEditCtrl'
        })
        .when('/copyright/search',{
            templateUrl: 'html/copyright-search.html',
            controller: 'CopyrightSearchCtrl'
        })
    	.when('/ccc',{
    		templateUrl: 'html/ccc.html',
    		controller: 'CccCtrl'
    	})
    	.when('/ccc/edit',{
    		templateUrl: 'html/ccc-edit.html',
    		controller: 'CccEditCtrl'
    	})
		.when('/ccc/view',{
			templateUrl: 'html/ccc-view.html',
			controller: 'CccViewCtrl'
		})
    	.when('/ccc/search',{
    		templateUrl: 'html/ccc-search.html',
    		controller: 'CccSearchCtrl'
    	})
    	.when('/404',{
    		templateUrl: 'html/404.html'
    	})
    	.when('/unauthorized', {
            templateUrl: 'html/unauthorized.html'
        })
    	.otherwise({
            redirectTo: '/404'
        })
    }
]) 
.run(['$rootScope', 'toastr','$http','$location', function ($rootScope, Toastr, $http,$location) {
	$rootScope.user = window.user;
	delete window.user;
	
	$rootScope.$on('$routeChangeStart', function(event, toState, toParams, fromState, fromParams){
		//TODO 取得用户当前登录状态
		if(!$rootScope.user){
			event.preventDefault();// 取消默认跳转行为
			$location.path("/login"); //跳转到登录界面
		}
	});
}]);


