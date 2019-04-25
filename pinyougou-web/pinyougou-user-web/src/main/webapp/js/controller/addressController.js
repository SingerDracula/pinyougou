app.controller('addressController', function($scope,$controller, baseService){

    $controller('baseController', {$scope:$scope});

    $scope.showAddress = function (loginName) {
        baseService.sendGet("address/findAddress?loginName="+loginName).then(function (response) {
            $scope.address = response.data;
        })
    }
})