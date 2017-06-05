'use strict';

angular.module('enterprise-quality')
    .controller('CopyrightCtrl',['$scope','$location','$http','$modal','$q','toastr','FileUploadService',
        function($scope, $location, $http, $modal, $q, Toastr, FileUploadService){

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
                	if(res.code==0){
                		$scope.list = res.listContent.list;
                		$scope.pagination.totalElements = res.listContent.totalElements;
                		for(var i=0; i<$scope.list.length; i++){
                			$scope.list[i].hasCrURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].crUrl)? true : false;
                			$scope.list[i].hasCdURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].cdUrl)? true : false;
                			$scope.list[i].hasRgURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].rgUrl)? true : false;
                			$scope.list[i].hasEpURL = /.*(http|https|cert.hikvision.com.cn).*/.test($scope.list[i].epUrl)? true : false;
                		}
                	}else{
                		//TODO other exceptions
                		Toastr.error("系统繁忙");
                	}
                }).error(function(res, status, headers, config){
                	Toastr.error("AjaxError: "+ status);
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


            /** 
             * 查看详情
             */
            $scope.view = function(item){
            	$location.url('/copyright/view?id='+item.id);
            }
            
            //TODO complete fields
            $scope.fields = [{
            	name: '全部',
            	value: ''
            }, {
            	name:'软件名称',
            	value: '1'
            }, {
            	name:'简称',
            	value: '2'
            }, {
            	name:'软著登记号',
            	value: '3'
            }, {
            	name:'软件产品登记证书',
            	value: '4'
            }, {
            	name:'软件测评报告号',
            	value: '5'
            }, {
            	name:'类别界定报告号',
            	value: '6'
            }, {            	
            	name:'软件型号',
            	value: '7'
            }]
            	
            $scope.searchInput = {
            	"field":"",
            	"keyword":""
            };
            
            /**  
    	     *   搜索
    	     */
            $scope.search = function (input) {
                $location.url('/copyright/search?f='+input.field+'&kw='+input.keyword);
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
                    url: '/fileupload/indexlist.do',
                    data: fd,
                    headers: {
                    	'Accept':'*/*',
                    	'Content-Type':undefined
                    }
                }).success(function(res) {
                	if(res.code == 0){
                		Toastr.success('更新公检记录'+res.numOfInspections+"条，双证记录"+res.numOfCopyRight+"条，3C记录"+res.numOf3C+"条");
                		$scope.removeFile();
                    	getList(1, $scope.pagination.size);
                    } else{
                    	Common.retCodeHandler(res.code);
                    }
                }).error(function(res) {
                	Toastr.error('Submit ajax failure');
                    defer.reject();
                });
                return defer.promise;
            };

            $scope.removeFile = function(){
            	$scope.file = {};
                $scope.fileName = '';
    		};
    		
        }]);