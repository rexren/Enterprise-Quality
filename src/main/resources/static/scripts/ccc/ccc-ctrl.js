'use strict';

angular.module('enterprise-quality').controller('CccCtrl', ['$scope', '$rootScope','$location', '$http', '$modal', '$q', 'toastr', 'FileUploadService', 'common',
    function($scope, $rootScope, $location, $http, $modal, $q, Toastr, FileUploadService, Common) {
		$scope.authority = $rootScope.user.authorities[0]?$rootScope.user.authorities[0].authority: null;
    
        $scope.pagination = {
            page: 1,
            size: 10,
            totalElements: 0
        };
        getList($scope.pagination.page, $scope.pagination.size);

        $scope.onPageChange = function(page) {
            $scope.pagination.page = page;
            getList($scope.pagination.page, $scope.pagination.size);
        };

        function getList(page, size) {
            var param = {
                pageNum: page,
                pageSize: size
            };
            $http.get('/ccc/list.do', {
                params: param
            }).success(function(res) {
                if (res.code == 0) {
                	$scope.list = res.listContent?res.listContent.list:null;
    	            $scope.pagination.totalElements = res.listContent?res.listContent.totalElements:null;
    	            if($scope.list && $scope.list.length > 0){
    	            	for (var i = 0; i < $scope.list.length; i++) {
    	            		$scope.list[i].hasURL = /.*(http|https).*/.test($scope.list[i].url) ? true : false;
    	            	}
                    }
                } else {
                    Toastr.error(res.msg);
                }
            }).error(function(res, status) {
            	Toastr.error("getListByAjax error: " + status);
            })
        }

        $scope.edit = function(item) {
            if (item) {
                $location.url('/ccc/edit?id=' + item.id);
            }
            else {
                $location.url('/ccc/edit');
            }
        }

        /** 
         * 查看详情
         */
        $scope.view = function(item) {
            $location.url('/ccc/view?id=' + item.id);
        }

        $scope.searchInput = {
            "field": "",
            "keyword": ""
        };

        /**  
         *   搜索
         */
        $scope.search = function(input) {
        	if(input.keyword=='')
        		Toastr.error('请输入搜索关键字');
        	else		
        		$location.url('/ccc/search?f=' + input.field + '&kw=' + input.keyword);
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
        $scope.importList = function() {
            var defer = $.Deferred();
            defer.progress(function(file) {
                $scope.file = file;
                $scope.fileName = file.name;
                $scope.$apply();
            });
            defer.params = {
                accept: null //接收所有文件类型
            };
            FileUploadService.open('file', defer);
        };

        /**  
         *  file upload
         */
        $scope.submit = function() {
            var defer = $q.defer();
            var fd = new FormData();
            fd.append('file', $scope.file);
            $http({
                method: 'POST',
                url: '/fileupload/indexlist.do',
                data: fd,
                headers: {
                    'Accept': '*/*',
                    'Content-Type': undefined
                }
            }).success(function(res) {
                if (res.code == 0) {
                    Toastr.success('更新公检记录' + res.numOfInspections + "条，双证记录" + res.numOfCopyRight + "条，3C记录" + res.numOf3C + "条");
                    $scope.removeFile();
                    getList(1, $scope.pagination.size);
                } else {
                	Toastr.error(res.msg);
                }
            }).error(function(res) {
                Toastr.error('Submit ajax failure');
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
            name: '文件编号',  //docNo
            value: '1'
        }, {
            name: '产品型号',  //model
            value: '2'
        }, {
            name: '产品名称',  //productName
            value: '3'
        }, {
            name: '备注',   //remarks
            value: '4'
        }]
    }
]);