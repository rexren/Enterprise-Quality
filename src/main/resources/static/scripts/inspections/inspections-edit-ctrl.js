'use strict';

angular.module('enterprise-quality').controller('InspectionsEditCtrl',['$scope', '$location', '$http', '$q', 'toastr', 'FileUploadService', 'common',
    function($scope, $location, $http, $q, Toastr, FileUploadService, Common) {
        $scope.formData = {
            'model': '',
            'name': '',
            'version': '',
            'testType': '',
            'company': '',
            'basis': '',
            'awardDate': '',
            'docNo': '',
            'certUrl': '',
            'organization': '',
            'remarks': '',
            'operator': ''
        };
        $scope.contentList = [];
        $scope.fileName = '';
        var targetUrl = '/inspections/save.do';

        $scope.inspectionId = $location.search().id;
        if ($scope.inspectionId) {
            var param = {
                id: $scope.inspectionId
            };
            $http.get('/inspections/detail.do', {
                params: param
            }).success(function(res) {
                var adate = Date.parse(Date(res.awardDate));
                adate = adate > 32503651200 ? new Date(adate) : new Date(adate * 1000);
                $scope.formData = {
                    'model': res.model,
                    'name': res.name,
                    'version': res.version,
                    'testType': res.testType,
                    'company': res.company,
                    'basis': res.basis,
                    'awardDate': adate,
                    'docNo': res.docNo,
                    'certUrl': res.certUrl,
                    'organization': res.organization,
                    'remarks': res.remarks,
                    'operator': res.operator
                };
                $scope.fileName = res.docFilename;
            }).error(function(res, status, headers, config) {
            	Toastr.error("getListByAjax error: " + status);
            });

            targetUrl = '/inspections/update.do';
        }

        /**  
         *  file upload handler
         */
        $scope.openFile = function() {
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
        }

        $scope.submit = function() {
            if ($scope.formData.model == '') {
            	Toastr.error('请输入产品型号');
            } else if ($scope.formData.name == '') {
            	Toastr.error('请输入软件名称');
            }else if (!$scope.formData.awardDate || $scope.formData.awardDate == '') {
            	Toastr.error('请选择颁发日期');
            } else {
                var defer = $q.defer();
                var fd = new FormData();
                fd.append('file', $scope.file);
                fd.append('fileName', $scope.fileName);
                if ($scope.inspectionId) {
                    fd.append('id', $scope.inspectionId);
                }
                for (var i in $scope.formData) {
                    fd.append(i, $scope.formData[i]);
                }
            	var awardTimeStamp = Date.parse($scope.formData.awardDate) / 1000;
            	fd.set('awardDate', awardTimeStamp);
                $http({
                    method: 'POST',
                    url: targetUrl,
                    data: fd,
                    headers: {
                        'Accept': '*/*',
                        'Content-Type': undefined
                    }
                }).success(function(res) {
                    if (res.code == 0) {
                    	Toastr.success('Submit successfully');
                    	$location.url('/inspections');
                    } else {
                        Common.retCodeHandler(res.code);
                    }
                }).error(function(res) {
                	Toastr.error('HTTP failure');
                    console.log('Error msg:');
                    console.log(res);
                    defer.reject();
                });
                return defer.promise;
            }
        };


        $scope.removeFile = function() {
            $scope.file = {};
            $scope.fileName = '';
        };

        // back
        $scope.back = function() {
        	window.history.back();
        }

    }]);