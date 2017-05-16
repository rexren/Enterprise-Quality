'use strict';

angular.module('enterprise-quality').controller('InspectionsEditCtrl', ['$scope','$location','$http','$resource',
	function($scope, $location, $http, $resource){
	
		/**  
	     *  file upload handler
	     */
		//todo 上传之前必须输入正确的文件编号！
	    var Files = $resource('/inspections/upload', { docNo: "@docNo" });
	
		angular.extend($scope, {
			file: null ,
			upload: function(file) {
				Files.prototype.$save.call(file, function(self, headers) {
					// Handle server response
					console.log(self);
					console.log(headers);
				});
			}
		});
		
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
	        	var adate = Date.parse(Date(res.awardDate));
	        	adate = adate>32503651200? new Date(adate) : new Date(adate*1000);
	        	console.log('adate:'+adate);
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
              
        $scope.submit = function () {
            console.log($scope.formData);
        };
        // back
        $scope.back = function(){
            $location.url('/inspections');
        }

    }]);