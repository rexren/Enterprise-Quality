'use strict';

angular.module('enterprise-quality').controller('InspectionsEditCtrl', 
	function($scope, $location, $http, $q, FileUploadService){
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
	    $scope.fileName = '';
	    var targetUrl='/inspections/save.do';
	    
        $scope.inspectionId = $location.search().id;
        if($scope.inspectionId){
        	var param = {id: $scope.inspectionId};
	        $http.get('/inspections/detail.do',{params:param}).success(function(res){
	        	var adate = Date.parse(Date(res.awardDate));
	        	adate = adate>32503651200? new Date(adate) : new Date(adate*1000);
	        	$scope.formData = {
	    	        'model': res.model,
	    	        'name': res.name,
	    	        'version': res.version,
	    	        'testType': res.testType,
	    	        'company': res.company,
	    	        'basis': res.basis,
	    	        'awardDate': adate,
	    	        'docNo': res.docNo,
	    	        'certUrl': res.certUrl,
	    	        'organization': res.organization,
	    	        'remarks': res.remarks,
	    	        'operator': res.operator
	        	}
		    }).error(function(res, status, headers, config){
		        alert("getListByAjax error: "+status);
		    })

        	targetUrl='/inspections/update.do';
        }

		/**  
	     *  file upload handler
	     */
		$scope.openFile=function(){
	        var defer = $.Deferred();
	         defer.progress(function(file){
	            $scope.file=file;
	            $scope.fileName =file.name;
	            $scope.$apply();
	         });
	        defer.params={
	            accept:null //接收所有文件类型
	        };
	        FileUploadService.open('file',defer);
	    }
		
        $scope.submit = function () {
        	var awardDateTimeStamp = Date.parse($scope.formData.awardDate)/1000;
            var defer = $q.defer();
            var fd = new FormData();
            fd.append('file',$scope.file);
            if($scope.inspectionId){
            	fd.append('id',$scope.inspectionId);
            }
            for(var i in $scope.formData){
                fd.append(i,$scope.formData[i]);
            }
            fd.set('awardDate', awardDateTimeStamp);
            $http({
                method: 'POST',
                url: targetUrl,
                data: fd,
                headers: {
                	'Accept':'*/*',
                	'Content-Type':undefined
                }
            }).success(function(res) {
            	if(res.code<400 & res.code>=200){
                	alert('Submit successfully');
                } else{
                	if(res.code == '501') {
                		alert('错误：文件被加密，请上传未加密的文件');                		
                	}else{
                		alert('错误：文件格式有误！'); 
                	}
                } 
            	$location.url('/inspections');
            }).error(function(res) {
            	alert('Submit failure');
            	console.log('Error msg:');
            	console.log(res);
                defer.reject();
            });
            return defer.promise;
        };
        

        $scope.removeFile = function(){
        	$scope.file = {};
            $scope.fileName = '';
		};
        
        // back
        $scope.back = function(){
            $location.url('/inspections');
        }

    });