'use strict';

angular.module('enterprise-quality').controller('deleteModalCtrl',['$scope', '$modalInstance', 'data', 'toastr',
    function ($scope, $modalInstance, data, Toastr) {
    
    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.confirm = function () {
        $modalInstance.close();
    };
}
]);
