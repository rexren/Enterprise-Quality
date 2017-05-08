angular.module('enterprise-quality-app',['ngRoute'])
       .config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/',{template:'这是首页页面'})
            .when('/hello', {    //管理员管理
                templateUrl: 'html/hello.html',
                controller: 'helloCtrl'
            })
            .otherwise({
                redirectTo: '/hello'
            })
       }]);
    .run(['$rootScope', '$location', 'common', function ($rootScope, $location, Common) {
    	// todo
    }]);
