'use strict';

angular.module('enterprise-quality')
    .controller('InspectionsCtrl', ['$scope','$location','$http',
    	function($scope, $location, $http){
    	
    	$scope.DoCtrlPagingAct = function(logText){
    		console.log(logText);
    	}

    	// todo 先确定页码，再getList
    	getList(1, 10);
    	
        function getList(page, size) {
            var param = {pageNum:page,pageSize:size};
            $http.get('/inspections/list.action',{params:param}).success(function(res){
                console.log("success");
                console.log(res);
                $scope.list = res.content;
                // todo $scope.pagination = data.pagination;
	        }).error(function(res, status, headers, config){
	            console.log("getListByAjax error: "+status);
	        })
        }

        $scope.edit = function(id){
        	if(id){
        		$location.url('/inspections/edit?id='+id);
        	}
        	// 新建
        	else{
            	$location.url('/inspections/edit');
        	}
        }
    }]);