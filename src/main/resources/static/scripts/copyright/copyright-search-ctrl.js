'use strict';

angular.module('enterprise-quality').controller('CopyrightSearchCtrl', ['$scope','$location','$http','$q','toastr',
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

        $scope.searchInput = {
        	"field": $location.search().f,
        	"keyword":$location.search().kw
        };
        
        console.log($scope.searchInput);
        
    }]);