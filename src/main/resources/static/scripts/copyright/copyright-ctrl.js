'use strict';

angular.module('enterprise-quality')
    .controller('CopyrightCtrl',['$scope','$rootScope','$location','$http','$modal','$q','toastr','FileUploadService',
        function($scope, $rootScope, $location, $http, $modal, $q, Toastr, FileUploadService){
    	$scope.authority = $rootScope.user.roles?$rootScope.user.roles[0]: null;
    	$scope.pagination = {
            page: 1,
            size: 10,
            totalElements: 0
        };
        $scope.isLoading = false;
        getList($scope.pagination.page, $scope.pagination.size);

        $scope.onPageChange = function(page){
            $scope.pagination.page = page;
            getList($scope.pagination.page, $scope.pagination.size);
        };
        function getList(page, size) {
            var param = {pageNum:page,pageSize:size};
            $scope.isLoading = true;
            $http.get('/copyright/list.do',{params:param}).success(function(res){
            	if(res.code==0){
            		$scope.list = res.listContent?res.listContent.list:null;
    	            $scope.pagination.totalElements = res.listContent?res.listContent.totalElements:null;
            		if($scope.list && $scope.list.length > 0){
            			 for(var i=0; i<$scope.list.length; i++){
                			$scope.list[i].hasCrURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].crUrl)? true : false;
                			$scope.list[i].hasCdURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].cdUrl)? true : false;
                			$scope.list[i].hasRgURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].rgUrl)? true : false;
                			$scope.list[i].hasEpURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].epUrl)? true : false;
            			 }
            		}
            	}else{
            		if(res.msg){
    	        		Toastr.error(res.msg);
    	        	} else {
    	        		Toastr.error('登录过期，请刷新重新登录');
    	        		window.location.href='/login';
    	        	}
            	}
            	$scope.isLoading = false;
            }).error(function(res, status, headers, config){
            	Toastr.error("AjaxError: "+ status);
            	$scope.isLoading = false;
            })
        }
        
        /** 
         * 编辑
         */
        $scope.edit = function(item){
            if(item){
                $location.url('/copyright/edit?id='+item.id);
            }
            // 新建
            else{
                $location.url('/copyright/edit');
            }
        }

        /** 
         * 查看详情
         */
        $scope.view = function(item){
        	$location.url('/copyright/view?id='+item.id);
        }
        
        $scope.searchInput = {
        	"field":"",
        	"keyword":""
        };
        
        /**  
	     *   搜索
	     */
        $scope.search = function (input) {
        	if(input.keyword=='')
        		Toastr.error('请输入搜索关键字');
        	else
        		$location.url('/copyright/search?f='+input.field+'&kw='+input.keyword);
        };
        
        $scope.enterEvent = function(e) {
            var keycode = window.event?e.keyCode:e.which;
            if(keycode==13){
            	e.preventDefault();
                $scope.search($scope.searchInput);
            }
        }
        
        /**  
	     *   导入excel文件列表
	     */
        $scope.importList = function () {
            var defer = $.Deferred();
	         defer.progress(function(file){
	            $scope.file=file;
	            $scope.fileName =file.name;
	            console.log($scope.fileName);
	            $scope.$apply();
	         });
	        defer.params={
	            accept:null //接收所有文件类型
	        };
	        FileUploadService.open('file',defer);
        };
        
        /**  
	     *  file upload
	     */
        $scope.submit = function () {
        	$scope.isLoading = true;
            var defer = $q.defer();
            var fd = new FormData();
            fd.append('file',$scope.file);
            $http({
                method: 'POST',
                url: '/fileupload/indexlist.do',
                data: fd,
                headers: {
                	'Accept':'*/*',
                	'Content-Type':undefined
                }
            }).success(function(res) {
            	if(res.code == 0){
            		Toastr.success('更新资质记录'+res.numOfInspections+"条，双证记录"+res.numOfCopyRight+"条，3C记录"+res.numOf3C+"条");
            		$scope.removeFile();
                	getList(1, $scope.pagination.size);
                } else{
                	if(res.msg){
    	        		Toastr.error(res.msg);
    	        	} else {
    	        		Toastr.error('登录过期，请刷新重新登录');
    	        		window.location.href='/login';
    	        	}
                }
            	$scope.isLoading = false;
            }).error(function(res) {
            	Toastr.error('Submit ajax failure');
            	$scope.isLoading = false;
                defer.reject();
            });
            return defer.promise;
        };

        $scope.removeFile = function(){
        	$scope.file = {};
            $scope.fileName = '';
		};

        $scope.fields = [{
        	name: '全部',
        	value: ''
        }, {
        	name:'软件名称',  //softwareName
        	value: '1'
        }, {     
        	name:'简称',  //abbreviation
        	value: '2'
        }, {       	
        	name:'软件型号',   //model
        	value: '3'
        }, {
        	name:'软著登记号', //crNo
        	value: '4'
        }, {
        	name:'软件产品登记证书', //rgNo
        	value: '5'
        }, {
        	name:'软件测评报告号', //epNo
        	value: '6'
        }, {
        	name:'类别界定报告号',  //cdNo
        	value: '7'
        }]
        	
    }]);