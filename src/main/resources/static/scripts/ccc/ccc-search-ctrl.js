'use strict';

angular.module('enterprise-quality').controller('CccSearchCtrl', ['$scope','$location','$http','$q','toastr',
    function($scope, $location, $http, $q, Toastr){
        $scope.pagination = {
            page: 1,
            size: 20,
            totalElements: 0
        };

        $scope.onPageChange = function(page){
            $scope.pagination.page = page;
            getList($scope.pagination.page, $scope.pagination.size);
        };

        //todo url中读取searchInput
        $scope.searchInput = {
        	"field": $location.search().f,
        	"keyword":$location.search().kw
        };
       
        $http.get('/ccc/search.do', {
            params: $scope.searchInput
        }).success(function(res) {
            if (res.code == 0) {
                $scope.list = res.listContent.list;
                $scope.pagination.totalElements = res.listContent.totalElements;
                for (var i = 0; i < $scope.list.length; i++) {
                    $scope.list[i].hasURL = /.*(http|https).*/.test($scope.list[i].url) ? true : false;
                }
            } else {
                //TODO other exceptions
                Toastr.error("系统繁忙");
            }
        }).error(function(res, status) {
            Toastr.error("getListByAjax error: " + status);
        })
        
        
    }]);