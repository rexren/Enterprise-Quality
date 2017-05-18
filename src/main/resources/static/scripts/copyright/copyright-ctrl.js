'use strict';

angular.module('enterprise-quality')
    .controller('CopyrightCtrl',['$scope','$location','$http',
        function($scope,$location,$http){

            $scope.importList = function () {
                //导入excel文件
                console.log('批量导入');
            };

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
                $http.get('/copyright/list.action',{params:param}).success(function(res){
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
        }]);