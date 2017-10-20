'use strict';

angular.module('enterprise-quality').controller('InspectionsCtrl', ['$scope','$rootScope','$location','$http','$modal','$q','toastr','FileUploadService','common',
    function($scope, $rootScope, $location, $http, $modal, $q, Toastr, FileUploadService, Common){
        $scope.authority = $rootScope.user.roles?$rootScope.user.roles[0]: null;
        $scope.isLoading = false;
		$scope.pagination = {
            page: 1,
            size: 10,
            totalElements: 0
        };

        $scope.searchInput = {
        	"field":"",
        	"keyword":"",
        	"searchRelation":"AND",
        	"contentKeyword":"",
        	"contentKeywordRelation":"AND"  //默认是与关系
        };
        
        $scope.timeRange = {
        	"start":"",
        	"end":""
        };
        
        getList($scope.pagination.page, $scope.pagination.size);

        $scope.onPageChange = function(page){
            $scope.pagination.page = page;
            getList($scope.pagination.page, $scope.pagination.size, $scope.timeRange.start, $scope.timeRange.end);
        };
 
        function getList(page, size, start, end) {
            var param = {
            	pageNum:page,
            	pageSize:size,
                startTime: start? Date.parse(start) : null,
                endTime: end? Date.parse(end) : null
            };
            
            $scope.isLoading = true;
            $http.get('/inspections/list.do',{params:param}).success(function(res){
            	if(res.code==0){
            		$scope.list = res.listContent?res.listContent.list:null;
    	            $scope.pagination.totalElements = res.listContent?res.listContent.totalElements:null;
    	            if($scope.list && $scope.list.length > 0){
    	            	for(var i=0; i<$scope.list.length;i++){
    	            		$scope.list[i].hasURL = /.*(http|https).*/.test($scope.list[i].certUrl)? true : false;          
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
            	Toastr.error("网络错误");
            	$scope.isLoading = false;
            })
        };
        
        $scope.timeSearch = function(){
        	// 重置页码
        	$scope.pagination = {
                page: 1,
                size: 10,
                totalElements: 0
            };
        	getList($scope.pagination.page, $scope.pagination.size, $scope.timeRange.start, $scope.timeRange.end);
        }
        
        /**
		 * 编辑单条数据
		 */
        $scope.edit = function(item){
            if(item){
                $location.url('/inspections/edit?id='+item.id);
            }
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
        
        /**
		 * 搜索
		 */
        $scope.search = function (input) {
        	if($scope.searchInput.keyword==''&&$scope.searchInput.contentKeyword=='')
        		Toastr.error('请输入至少一个关键字');
        	else
        		$location.url('/inspections/search?f='+input.field+'&kw='+input.keyword+'&sr='+input.searchRelation+'&c='+input.contentKeyword+'&cr='+input.contentKeywordRelation);
        };
        
        $scope.enterEvent = function(e) {
            var keycode = window.event?e.keyCode:e.which;
            if(keycode==13){
            	e.preventDefault();
                $scope.search($scope.searchInput);
            }
        }
        
        /**
		 * 导入excel文件列表
		 */
        $scope.importList = function () {
            var defer = $.Deferred();
	         defer.progress(function(file){
	            $scope.file=file;
	            $scope.fileName =file.name;
	            $scope.$apply();
	         });
	        defer.params={
	            accept:null // 接收所有文件类型
	        };
	        FileUploadService.open('file',defer);
        };
        
        /**
		 * file upload
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
            	$scope.isLoading = false;
            	Toastr.error('网络错误');
                defer.reject();
                window.location.href='/login';
            });
            return defer.promise;
        };

        $scope.removeFile = function(){
        	$scope.file = {};
            $scope.fileName = '';
		};
		
		/**
		 * 删除条目
		 */
		$scope.delete = function(itemId) {
			// 打开弹窗
			var modalInstance = $modal.open({
		        animation: true,
		        templateUrl: '/html/modal-delete.html',
		        controller: 'deleteModalCtrl',
		        size: 'sm',
		        resolve: {
		        	data: function () {
		        		return itemId;
		        	}
		        }
		    });
			// 弹窗在关闭的时候执行的
			modalInstance.result.then(function () {
				// $modalInstance.close
				$scope.isLoading = false;
				$http.get('/inspections/delete.do',{params:{id:itemId}}).success(function(res){
					if(res.code == 0){
						Toastr.success('删除成功');
						getList($scope.pagination.page, $scope.pagination.size);
					}else{
						if(res.msg){
			        		Toastr.error(res.msg);
			        	} else {
			        		Toastr.error('登录过期，请重新登录');
			        	}
						$scope.isLoading = false;
					}
				}).error(function(res, status, headers, config) {
	            	Toastr.error("AjaxError: "+ status);
	            	$scope.isLoading = false;
	            });
			}, function () {
				// $modalInstance.dismiss
				$scope.isLoading = false;
			});
	    };

		$scope.fields = [{
        	name: '全部',
        	value: ''
        }, {
        	name:'产品型号', // model
        	value: '1'
        }, {
        	name:'软件名称', // name
        	value: '2'
        }, {
        	name:'测试类别', // testType
        	value: '3'
        }, {
        	name:'测试依据', // basis
        	value: '4'
        }, {
        	name:'文件编号', // docNo
        	value: '5'
        }, {
        	name:'认证机构', // organization
        	value: '6'
        }, {
        	name:'备注', // remarks
        	value: '7'
        }];
    }]);