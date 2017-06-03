'use strict';

angular.module('enterprise-quality',[
	'ngAnimate',
    'ngRoute',
    'ui.bootstrap',
    'toastr'
    ])
.config(['$routeProvider', function($routeProvider){
    $routeProvider
    	.when('/',{
    		templateUrl:'html/home.html',
    		controller:'HomeCtrl'
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
    	.when('/ccc/search',{
    		templateUrl: 'html/ccc-search.html',
    		controller: 'CccSearchCtrl'
    	})
    	.when('/components/form',{
    		templateUrl: 'html/components/form.html',
    		controller: 'FormCtrl'
    	})
    	.otherwise({
            redirectTo: '/inspections'
        })
    }
])
;
