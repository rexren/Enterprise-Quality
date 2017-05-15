'use strict';

angular.module('enterprise-quality').controller('InspectionsEditCtrl', ['$scope','$location','$http',
	function($scope, $location, $http){
	    $scope.formData = 
	    {
	        'model': '',
	        'name': '',
	        'version': '',
	        'testType': '',
	        'company': '',
	        'basis': '',
	        'awardDate': '',
	        'docNo': '',
	        'certUrl': '',
	        'organization': '',
	        'remarks': '',
	        'operator': ''
	    };
	    
        var urlId = $location.search().id;
        if(urlId){
        	var param = {id: urlId};
	        $http.get('/inspections/detail.action',{params:param}).success(function(res){
	        	console.log('success');
	        	console.log(res);
	        	//TODO $scope.formData
		    }).error(function(res, status, headers, config){
		        alert("getListByAjax error: "+status);
		    })
        }
              
        $scope.submit = function () {
            console.log($scope.formData);
        };
        // back
        $scope.back = function(){
            $location.url('/inspections');
        }

    }]);