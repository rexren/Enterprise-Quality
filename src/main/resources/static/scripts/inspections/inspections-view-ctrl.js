'use strict';

angular.module('enterprise-quality').controller('InspectionsViewCtrl', ['$scope','$rootScope','$location', '$http', '$q','toastr',
	function($scope,$rootScope, $location, $http, $q, Toastr){
		$scope.authority = $rootScope.user.authorities[0].authority;
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
	        $http.get('/inspections/detail.do',{params:param}).success(function(res){
	        	if(res.code == 0){
	        		var adate = Date.parse(Date(res.data.awardDate));
	        		adate = adate>32503651200? new Date(adate) : new Date(adate*1000);
	        		$scope.digest = {
        				'model': res.data.model,
        				'name': res.data.name,
        				'version': res.data.version,
        				'testType': res.data.testType,
        				'company': res.data.company,
        				'basis': res.data.basis,
        				'awardDate': adate,
        				'docNo': res.data.docNo,
        				'docFilename': res.data.docFilename,
        				'certUrl': res.data.certUrl,
        				'organization': res.data.organization,
        				'remarks': res.data.remarks,
        				'operator': res.data.operator
	        		};
	        		$scope.fileName = res.docFilename;
	        	} else{
	        		Toastr.error(res.msg);
            	} 
		    }).error(function(res, status, headers, config){
		    	Toastr.error("getListByAjax error: "+status);
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
		    		Toastr.error('检测报告：'+res.msg);
		    	}
		   	}).error(function(res, status, headers, config){
		   		Toastr.error("getListByAjax error: "+status);
		    });
        }
        

        // back
        $scope.back = function() {
        	window.history.back();
        }

    }]);