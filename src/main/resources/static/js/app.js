var app = angular.module('enterprise-quality',['ngRoute','helloContrls']);  
app.config(function($routeProvider){  
	$routeProvider
    .when('/hello',{ 
        templateUrl:'html/hello.html',  
        controller:'helloCtrl'  
    }).otherwise({  
        redirectTo:'/hello'  
    })  
});