'use strict';

angular.module('enterprise-quality').controller('InspectionsEditCtrl', ['$scope','$location','$http', '$q','FileUploadService',
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
	    $scope.fileName = '未上传';
	    
        var urlId = $location.search().id;
        if(urlId){
        	var param = {id: urlId};
	        $http.get('/inspections/detail.action',{params:param}).success(function(res){
	        	console.log('success');
	        	console.log(res);
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
            var defer = $q.defer();
            var fd = new FormData();
            fd.append('file',$scope.file);
            for(var i in $scope.formData){
                fd.append(i,$scope.formData[i]);
            }
            $http({
                method: 'POST',
                url: '/inspections/save.do',
                data: fd,
                headers: {
                	'Accept':'*/*',
                	'Content-Type':undefined
                }
            }).success(function(res) {
            	alert('Submit successfully ^_^');
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

    }]);