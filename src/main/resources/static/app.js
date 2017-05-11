'use strict';

angular.module('enterprise-quality',['ngRoute'])
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
    	.when('/copyright',{
    		templateUrl: 'html/copyright.html',
    		controller: 'CopyrightCtrl'
    	})
    	.when('/ccc',{
    		templateUrl: 'html/ccc.html',
    		controller: 'CccCtrl'
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
