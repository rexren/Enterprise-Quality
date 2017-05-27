'use strict';

angular.module('enterprise-quality').controller('InspectionsViewCtrl', 
	function($scope, $location, $http, $q, FileUploadService){
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
	        	var adate = Date.parse(Date(res.awardDate));
	        	adate = adate>32503651200? new Date(adate) : new Date(adate*1000);
	        	$scope.digest = {
	    	        'model': res.model,
	    	        'name': res.name,
	    	        'version': res.version,
	    	        'testType': res.testType,
	    	        'company': res.company,
	    	        'basis': res.basis,
	    	        'awardDate': adate,
	    	        'docNo': res.docNo,
	    	        'docFilename': res.docFilename,
	    	        'certUrl': res.certUrl,
	    	        'organization': res.organization,
	    	        'remarks': res.remarks,
	    	        'operator': res.operator
	        	};
	        	$scope.fileName = res.docFilename;
	        	
		    }).error(function(res, status, headers, config){
		        alert("getListByAjax error: "+status);
		    });

		    $http.get('/inspections/contents.do',{params:param}).success(function(res){
				if(res.length>0){
	        		// rearrange contentList data to the template
		        	$scope.contentHead  = [res[0].caseId, res[0].caseName, res[0].caseDescription];
		        	var cachedCaseId = "";
		        	var cachedCaseName = "";
		        	var cIndex = -1;
		        	for(var i = 1;i<res.length;i++){
		        		if(cachedCaseId!=res[i].caseId){
		        			cachedCaseId = res[i].caseId;
		        			cachedCaseName = res[i].caseName;
		        			$scope.contentList.push({
		        				'caseId':res[i].caseId,
		        				'caseName':res[i].caseName,
		        				'caseDescription': [res[i].caseDescription]
		        			});
		        			cIndex++;
		        		} else{
		        			$scope.contentList[cIndex].caseDescription.push(res[i].caseDescription);
		        		}
		        	}
	        	}
		   	}).error(function(res, status, headers, config){
		        alert("getListByAjax error: "+status);
		    });
        }
        

        // back
        $scope.back = function(){
            $location.url('/inspections');
        }

    });