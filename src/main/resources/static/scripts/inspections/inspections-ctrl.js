'use strict';

angular.module('enterprise-quality').controller('InspectionsCtrl', ['$scope','$location','$http','$modal','$q','toastr','FileUploadService','common',
    function($scope, $location, $http, $modal, $q, Toastr, FileUploadService, Common){
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
            	if(res.code==0){
            		$scope.list = res.listContent.list;
            		$scope.pagination.totalElements = res.listContent.totalElements;
            		for(var i=0; i<$scope.list.length;i++){
            			$scope.list[i].hasURL = /.*(http|https).*/.test($scope.list[i].certUrl)? true : false;          
            		}
            	}else{
            		//TODO other exceptions
            		Toastr.error("系统繁忙");
            	}
            }).error(function(res, status, headers, config){
            	Toastr.error("AjaxError: "+ status);
            })
        };
        
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
        
        /** 
         * 查看详情
         */
        $scope.view = function(item){
        	$location.url('/inspections/view?id='+item.id);
        }

        $scope.searchInput = {
        	"field":"",
        	"keyword":"",
        	"contentKeyword":""
        };
        
        /**  
	     *   搜索
	     */
        $scope.search = function (input) {
            $location.url('/inspections/search?f='+input.field+'&kw='+input.keyword+'&c='+input.contentKeyword);
        };
        
        
        /**  
	     *   导入excel文件列表
	     */
        $scope.importList = function () {
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
		
		$scope.fields = [{
        	name: '全部',
        	value: ''
        }, {
        	name:'产品型号',
        	value: '1'
        }, {
        	name:'软件名称',
        	value: '2'
        }, {
        	name:'测试类别',
        	value: '3'
        }, {
        	name:'测试依据',
        	value: '4'
        }, {
        	name:'文件编号',
        	value: '5'
        }, {
        	name:'认证机构',
        	value: '6'
        }, {
        	name:'备注',
        	value: '7'
        }]
    }]);