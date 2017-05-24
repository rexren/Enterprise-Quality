'use strict';

angular.module('enterprise-quality').controller('InspectionsCtrl', ['$scope','$location','$http','$modal','$q','FileUploadService',
    function($scope, $location, $http, $modal, $q, FileUploadService){
        $scope.pagination = {
            page: 1,
            size: 10,
            totalElements: 0
        };
        getList($scope.pagination.page, $scope.pagination.size);

        $scope.onPageChange = function(page){
            $scope.pagination.page = page;
            getList($scope.pagination.page, $scope.pagination.size);
        };

        function getList(page, size) {
            var param = {pageNum:page,pageSize:size};
            $http.get('/inspections/list.do',{params:param}).success(function(res){
                $scope.list = res.content;
                $scope.pagination.totalElements = res.totalElements;
            }).error(function(res, status, headers, config){
                alert("getListByAjax error: "+status);
            })
        }
        
        /** 
         * 编辑单条数据
         */
        $scope.edit = function(item){
            if(item){
                $location.url('/inspections/edit?id='+item.id);
            }
            // 新建
            else{
                $location.url('/inspections/edit');
            }
        };


        $scope.searchList = function () {
            //TODO 搜索列表结果展示
            console.log('TODO 搜索列表结果展示');
        };
        
        $scope.keywords = {};
        
        /** 
         * 高级搜索
         */
        $scope.advancedSearch = function () {
            //TODO 高级搜索
            console.log('TODO 高级搜索弹窗');
            // 打开弹窗
            var modalInstance = $modal.open({
            	animation: true,
            	templateUrl: 'html/inspections-search-modal.html',
            	controller: 'InspectionsSearchModalCtrl',
            	size: '',
            	resolve: {
            		keywords: function () {
            			return $scope.keywords;
            		}
            	}
            });

            // 弹窗在关闭的时候执行的
            modalInstance.result.then(function () {
              // $modalInstance.close 时候传递的参数
                console.log('Modal closed at: ' + new Date());
                console.log($scope.keywords);
            }, function () {
              // $modalInstance.dismiss 时候传递的参数
            	console.log('Modal dismissed at: ' + new Date());
            });
        };
        
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
            var defer = $q.defer();
            var fd = new FormData();
            fd.append('file',$scope.file);
            $http({
                method: 'POST',
                url: '/inspections/upload.do',
                data: fd,
                headers: {
                	'Accept':'*/*',
                	'Content-Type':undefined
                }
            }).success(function(res) {
            	if(res.code<400 & res.code>=200){
                	alert('上传成功');
                	$scope.fileName = '';
                	$scope.file = {};
                	getList(1, $scope.pagination.size);
                } else{
                	if(res.code == '501') {
                		alert('错误：文件被加密，请上传未加密的文件');                		
                	}else{
                		alert('错误：文件格式有误！'); 
                	}
                } 
            	//TODO 刷新列表 getList(1, $scope.pagination.size);
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
        
    }]);