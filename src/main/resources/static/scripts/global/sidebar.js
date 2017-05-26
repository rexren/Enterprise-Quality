'use strict';

angular.module('enterprise-quality').controller('sideBarCtrl', 
    function($scope, $location){
    	$scope.headerText = '检索分类';
    	$scope.menu = [
    		{
    			title: '公检&国标',
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
    	
    	for(var i=0;i<$scope.menu.length;i++){
    		var pattern = RegExp("\\"+$scope.menu[i].href);
    		if(pattern.test($location.url())){
    			$scope.menu[i].active = true;
    		}
    	}
    	
    	$scope.change = function(index){
    		for(var i=0;i<$scope.menu.length;i++){
    			$scope.menu[i].active = (i==index)? true: false;
        	}
    	}
    });