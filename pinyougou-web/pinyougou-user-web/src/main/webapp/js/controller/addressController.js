app.controller('userController', function($scope, baseService){
    $scope.showAddress = function () {
        baseService.sendGet("address/findAddress").then(function (response) {

        })
    }
}