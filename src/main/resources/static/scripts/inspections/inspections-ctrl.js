'use strict';

angular.module('enterprise-quality')
    .controller('InspectionsCtrl', ['$scope','$location','$http',
    	function($scope, $location, $http){

    	getList(1, 20);
    	
        function getList(page, size) {
            var param = {pageNum:page,pageSize:size};
            $http.get('/inspections/list.action',{params:param}).success(function(res){
                console.log("success");
                console.log(res);
                $scope.list = res.content;
                // todo $scope.pagination = data.pagination;
	        }).error(function(res, status, headers, config){
	            console.log("getListByAjax error");
	        })
        }
        
        // back
        $scope.addInspection = function(){
        	$location.url('/inspections/edit');
        }
    }]);