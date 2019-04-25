app.controller('addressController', function($scope,$controller,baseService){

    $controller('baseController', {$scope:$scope});

    $scope.showAddress = function () {
        alert($scope.loginName);
        baseService.sendGet("address/findAddress?loginName="+$scope.loginName).then(function (response) {
            $scope.address = response.data;
        })
    }
})