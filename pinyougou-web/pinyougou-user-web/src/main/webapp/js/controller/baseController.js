app.controller("baseController", function($scope,$controller,baseService){
    $scope.redirectUrl = window.encodeURIComponent(location.href);

    $scope.showName = function () {

        baseService.sendGet("/user/login").then(function (response) {
            $scope.loginName = response.data;
        })

    }
})