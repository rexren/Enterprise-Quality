'use strict';

angular.module('enterprise-quality').controller('sideBarCtrl',
    function($scope, $rootScope, $http, $location){
    
		$scope.userName = $rootScope.user? $rootScope.user.userName:null;

    	$rootScope.menu = [
    		{
    			title: '更新记录',
    			href: '/updatelogs',
    			icon:'fa-newspaper-o',
    			active: false
    		}, {
    			title: '资质查询',
    			href: '/inspections',
    			icon:'fa-laptop',
    			active: false
    		}, {
    			title: '双证',
    			href: '/copyright',
    			icon:'fa-files-o',
    			active: false
    		}, {
    			title: '3C',
    			href: '/ccc',
    			icon:'fa-th',
    			active: false  			
    		}
    	];
    	
    	
    	$rootScope.$on('$routeChangeStart', function(event, toState, toParams, fromState, fromParams){
    		for(var i=0;i<$rootScope.menu.length;i++){
    			var pattern = RegExp("\\"+$rootScope.menu[i].href);
    			if(pattern.test($location.url())){
    				$rootScope.menu[i].active = true;
    			}else{
    				$rootScope.menu[i].active = false;
    			}
    		}
    	})
    	
    	$scope.change = function(index){
    		//如果目标=当前位置，则刷新
    		for(var i=0;i<$rootScope.menu.length;i++){
    			var pattern = RegExp("\\"+$rootScope.menu[i].href);
    			if(pattern.test($location.url())& i==index){
    				location.reload();
    			}
    		}
    	}
    	
    
    });