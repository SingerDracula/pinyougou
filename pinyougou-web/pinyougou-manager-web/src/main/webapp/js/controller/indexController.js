app.controller("indexController",function ($scope,$controller,baseService) {
    $controller("baseController",{$scope:$scope});

    $scope.showLoginName = function () {
        baseService.sendGet("/getLoginName").then(function (value) {
            $scope.loginName = value.data.loginName;
        })
    }

})