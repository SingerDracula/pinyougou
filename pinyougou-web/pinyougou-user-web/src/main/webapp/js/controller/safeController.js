app.controller('sefaController', function($scope,$controller, baseService) {
        $controller('baseController', {$scope: $scope});

        $scope.changePassword = function () {
            if ($scope.user.username.length < 6) {
                alert("用户名长度必须大于或者等于6");
                return;
            }
            if ($scope.user.newPassword != $scope.user.confirmPassword) {
                alert("两次输入的密码不一致");
                return;
            }
            baseService.sendPost("safe/changePassword", $scope.user).then(function (response) {
                if (response.data) {
                    alert("修改成功")
                    $scope.user = {};
                } else {
                    alert("用户名或密码错误")
                }
            })
        }

        $scope.nextStep = function () {
            if($scope.vcode == null){
                alert("请输入验证码");
                return;
            }
            if($scope.smsCode == null){
                alert("请输入短信验证码");
                return;
            }
            baseService.sendGet("safe/check?vcode=" + $scope.vcode + "&smsCode=" + $scope.smsCode+"&phone="+$scope.phone).then(function (response) {
                if (response.data) {
                    location.href = "home-setting-address-phone.html";
                } else {
                    alert("验证码或短信验证码错误");
                }
            })
        }

    $scope.nextStep2 = function () {
        if($scope.vcode == null){
            alert("请输入验证码");
            return;
        }
        if($scope.smsCode == null){
            alert("请输入短信验证码");
            return;
        }
        baseService.sendGet("safe/check?vcode=" + $scope.vcode + "&smsCode=" + $scope.smsCode+"&phone="+$scope.newPhone).then(function (response) {
            if (response.data) {
                baseService.sendGet("safe/changePhone?phone="+$scope.newPhone).then(function (response) {
                    if (response.data){
                        location.href = "home-setting-address-complete.html";
                    } else {
                        alert("修改失败");
                    }
                })

            } else {
                alert("验证码或短信验证码错误");
            }
        })
    }

    $scope.sendCode = function () {
        if($scope.phone){
            baseService.sendGet("/user/sendCode?phone="+$scope.phone).then(function (response) {
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

    $scope.sendNewCode = function () {
        $scope.phone = $scope.newPhone;
        $scope.sendCode();
    }


    }

)