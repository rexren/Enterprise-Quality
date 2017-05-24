'use strict';

angular.module('enterprise-quality').controller('InspectionsEditCtrl', 
	function($scope, $location, $http, $q, FileUploadService){
		/*initialization*/
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
	    $scope.contentList = [];
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
	        	};
		    }).error(function(res, status, headers, config){
		        alert("getListByAjax error: "+status);
		    });
	        
/*	        //TODO '/inspections/contentTable.do'接口需完善
	        $http.get('/inspections/contentTable.do',{params:param}).success(function(res){
	        	$scope.contentList = res.contentList;
	        }).error(function(res, status, headers, config){
		        alert("getListByAjax error: "+status);
		    });
*/
        	targetUrl='/inspections/update.do';
        }
        
        /* mock*/
        $scope.contentList = [{
        	'caseId': 2,
        	'caseName':'检验项目1',
        	'caseDecription':[
        		'检验项目1的技术要求1检验项目1的技术要求1检验项目1的技术要求1检验项目1的技术要求1',
        		'检验项目1的技术要求2检验项目1的技术要求2检验项目1术要求2',
        		'检验项目1的技术要求3检验项目1的技术要求3检验项目1的技术要求的技术要求3',
        		'检验项目1的技术要求4检验项目1的技术要求4检验项目1的技术要求4检验项目1的技术要求4',
        		'检验项目1的技术要求5检验项目1的技术要求5检验项目1的技术要求5检验项目1的技术要求5',
        		],
        	'catalog':'',
        	'testResult':''
        },{
        	'caseId': 3,
        	'caseName':'检验项目2',
        	'caseDecription':[
        		'检验项目2的技术要求1检验项目2的技术要求1检验项目2的技术要求1检验项目2的技术要求要求1',
        		'检验项目2的技术要求2检验项目2的技术要求2检验项目2的技术要求2检验验项目2',
        		'检验项目2的技术要求3检验项目2的技术要求3检验项目2的技术要求3检验项目2的技术2的技术要求3',
        		'检验项目2的技术要求4检验项目2的技术要求4检验项目2的技术要求4检验项目2的技术要求4检验项目2的技术要求4',
        		'检验项目2的技术要求5检验项目2的技术要求5检验项目2的技术要求5检验项目2的技术要求5检验项目2的技术要求5检验项目2的技术要求5',
        		],
        	'catalog':'',
        	'testResult':''
        },{
        	'caseId': 4,
        	'caseName':'检验项目3',
        	'caseDecription':[
        		'检验项目3的技术要求1',
        		'检验项目3的技术要求2',
        		'检验项目3的技术要求3',
        		'检验项目3的技术要求4',
        		'检验项目3的技术要求5',
        		],
        	'catalog':'',
        	'testResult':''
        },{
        	'caseId': 4,
        	'caseName':'检验项目3',
        	'caseDecription':[
        		'检验项目3的技术要求1',
        		'检验项目3的技术要求2',
        		'检验项目3的技术要求3',
        		'检验项目3的技术要求4',
        		'检验项目3的技术要求5',
        		],
        	'catalog':'',
        	'testResult':''
        }]
        /* mock end*/

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
        	if($scope.formData.model==''){
        		alert('请输入产品型号');
        	} else if($scope.formData.name==''){
        		alert('请输入软件名称');
        	} else{
                var defer = $q.defer();
                var fd = new FormData();
                fd.append('file',$scope.file);
                if($scope.inspectionId){
                	fd.append('id',$scope.inspectionId);
                }
                for(var i in $scope.formData){
                    fd.append(i,$scope.formData[i]);
                }
                if($scope.formData.awardDate == ''){
            		fd.set('awardDate', 0);
            	} else{
                	var awardDateTimeStamp = Date.parse($scope.formData.awardDate)/1000;
                    fd.set('awardDate', awardDateTimeStamp);
            	}
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
        	}
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