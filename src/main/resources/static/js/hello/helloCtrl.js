var app = angular.module('enterprise', []); 
app.controller('helloCtrl', function($scope) {
    $scope.name = 'hello';
    $scope.helloClick = function() {
        console.log('hello click');
    };

});