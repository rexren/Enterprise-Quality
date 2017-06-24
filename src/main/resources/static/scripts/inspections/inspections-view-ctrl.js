'use strict';

angular.module('enterprise-quality').controller('InspectionsViewCtrl', ['$scope','$rootScope','$location', '$http', '$q','toastr',
	function($scope,$rootScope, $location, $http, $q, Toastr){
		$scope.authority = $rootScope.user.roles?$rootScope.user.roles[0]: null;
    	/*initialization*/
	    $scope.digest = 
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
	    $scope.contentList = [];
	    $scope.fileName = '';
	    $scope.viewType = 1;
	    
	    $scope.changeView = function(viewNo){
	    	$scope.viewType = viewNo;
	    }
	    
	    $scope.modify = function(){
	    	$location.url('/inspections/edit?id='+$location.search().id);
	    }
	    
	    $scope.currentLocation = $location.path();
        $scope.inspectionId = $location.search().id;
        if($scope.inspectionId){
        	var param = {id: $scope.inspectionId};
        	$scope.isLoading = true;
	        $http.get('/inspections/detail.do',{params:param}).success(function(res){
	        	if(res.code == 0){
	        		$scope.digest = {
        				'model': res.data.model,
        				'name': res.data.name,
        				'version': res.data.version,
        				'testType': res.data.testType,
        				'company': res.data.company,
        				'basis': res.data.basis,
        				'awardDate': res.data.awardDate,
        				'createDate': res.data.createAt,
        				'updateDate': res.data.updateAt,
        				'docNo': res.data.docNo,
        				'docFilename': res.data.docFilename,
        				'certUrl': res.data.certUrl,
        				'organization': res.data.organization,
        				'remarks': res.data.remarks,
        				'operator': res.data.operator
	        		};
	        		$scope.fileName = res.data.docFilename;
	        	} else{
	        		if(res.msg){
    	        		Toastr.error(res.msg);
    	        	} else {
    	        		Toastr.error('登录过期，请刷新重新登录');
    	        		window.location.href='/login';
    	        	}
            	}
	        	$scope.isLoading = false;
		    }).error(function(res, status, headers, config){
		    	Toastr.error("getListByAjax error: "+status);
		    	$scope.isLoading = false;
		    });

		    $http.get('/inspections/contents.do',{params:param}).success(function(res){
		    	if(res.code == 0){
					if(res.listContent && res.listContent.list.length>0){
		        		// rearrange contentList data to the template
			        	$scope.contentHead  = [res.listContent.list[0].caseId, res.listContent.list[0].caseName, res.listContent.list[0].caseDescription];
			        	var cachedCaseId = "";
			        	var cachedCaseName = "";
			        	var cIndex = -1;
			        	for(var i = 1;i<res.listContent.list.length;i++){
			        		if(cachedCaseId!=res.listContent.list[i].caseId){
			        			cachedCaseId = res.listContent.list[i].caseId;
			        			cachedCaseName = res.listContent.list[i].caseName;
			        			$scope.contentList.push({
			        				'caseId':res.listContent.list[i].caseId,
			        				'caseName':res.listContent.list[i].caseName,
			        				'caseDescription': [res.listContent.list[i].caseDescription]
			        			});
			        			cIndex++;
			        		} else{
			        			$scope.contentList[cIndex].caseDescription.push(res.listContent.list[i].caseDescription);
			        		}
			        	}
		        	}
		    	}else {
		    		if(res.msg){
    	        		Toastr.error(res.msg);
    	        	} else {
    	        		Toastr.error('登录过期，请刷新重新登录');
    	        		window.location.href='/login';
    	        	}
		    	}
		   	}).error(function(res, status, headers, config){
		   		Toastr.error("getListByAjax error: "+status);
		    });
        }
        
        $scope.edit = function() {
            $location.url('/inspections/edit?id='+$scope.inspectionId);
        }
        
        // back
        $scope.back = function() {
        	window.history.back();
        }

    }]);