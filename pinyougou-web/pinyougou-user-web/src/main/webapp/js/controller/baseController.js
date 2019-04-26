app.controller("baseController", function($scope,$controller,baseService){
    $scope.redirectUrl = window.encodeURIComponent(location.href);

    $scope.showName = function () {
        baseService.sendGet("/user/login").then(function (response) {
            $scope.loginName = response.data.username;
            $scope.phone = response.data.phone;
        })

    }

    $scope.sendCode = function () {
        if($scope.user.phone){
            baseService.sendGet("/user/sendCode?phone="+$scope.user.phone).then(function (response) {
                if (response.data){
                    alert("发送成功");
                } else {
                    alert("发送失败");
                }
            })
        }else {
            alert("请输入手机号码");
        }
    }
})