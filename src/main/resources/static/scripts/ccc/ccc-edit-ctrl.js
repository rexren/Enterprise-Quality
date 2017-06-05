'use strict';

angular.module('enterprise-quality')
    .controller('CccEditCtrl',['$scope','$location','$http','$modal','$q','toastr','FileUploadService','common',
        function($scope, $location, $http, $modal, $q, Toastr, FileUploadService, Common){

            $scope.formData = {
            	"docNo":"",
            	"model":"",
            	"productName":"",
            	"awardDate":"",
            	"expiryDate":"",
            	"url":"",
            	"organization":"",
            	"remarks":""
            };
            
            var urlId = $location.search().id;
            if(urlId){
            	var param = {id: urlId}; 
            	$http.get('/ccc/detail.do',{params:param}).success(function(res) {
					
            		var awardDate = Date.parse(Date(res.data.awardDate));
					awardDate = awardDate>32503651200? new Date(awardDate) : new Date(awardDate*1000);
					
					var expiryDate = Date.parse(Date(res.data.expiryDate));
					expiryDate = expiryDate>32503651200? new Date(expiryDate) : new Date(expiryDate*1000);
					
					$scope.formData = {
						"docNo":res.data.docNo,
		            	"model":res.data.model,
		            	"productName":res.data.productName,
		            	"awardDate":awardDate,
		            	"expiryDate":expiryDate,
		            	"url":res.data.url,
		            	"organization":res.data.organization,
		            	"remarks":res.data.remarks
					}
				}).error(function(res, status, headers, config){
    		        Toastr.error("getListByAjax error: "+status);
    		    })
            }
            
            //TODO save form
            $scope.submit = function () {
                console.log($scope.formData);
            };
            // back
            $scope.back = function(){
                $location.url('/ccc');
            }

        }]);