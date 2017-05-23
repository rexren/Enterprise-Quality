'use strict';

angular.module('enterprise-quality')
    .controller('CopyrightCtrl',['$scope','$location','$http','$modal','$q','FileUploadService',
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
                $http.get('/copyright/list.do',{params:param}).success(function(res){
                    $scope.list = res.content;
                    $scope.pagination.totalElements = res.totalElements;
                }).error(function(res, status){
                    alert("getListByAjax error: "+status);
                })
            }

            $scope.edit = function(item){
                if(item){
                    $location.url('/copyright/edit?id='+item.id);
                }
                // 新建
                else{
                    $location.url('/copyright/edit');
                }
            }

            $scope.searchList = function () {
                //TODO 搜索列表结果展示
                console.log('TODO 搜索列表结果展示');
            };
            
            $scope.keywords = {};
            
            $scope.advancedSearch = function () {
                //TODO 高级搜索
                console.log('TODO 高级搜索弹窗');
                // 打开弹窗
                var modalInstance = $modal.open({
                	animation: true,
                	templateUrl: 'html/copyright-search-modal.html',
                	controller: 'CopyRightSearchModalCtrl',
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
            	console.log('submit file');
            	// 导入excel文件 inspections/upload.do
            };

            $scope.removeFile = function(){
            	$scope.file = {};
                $scope.fileName = '';
    		};
    		
        }]);