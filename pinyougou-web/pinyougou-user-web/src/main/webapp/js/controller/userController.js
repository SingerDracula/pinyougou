/** 定义控制器层 */
app.controller('userController', function($scope,$controller, baseService){
    $controller('baseController', {$scope:$scope});

  $scope.register = function () {
      if($scope.user.password != $scope.password){
          alert("两次输入的密码不一致");
          return;
      }
      baseService.sendPost("/user/register?code="+$scope.code,$scope.user).then(function (response) {
          if (response.data){
              alert("注册成功");
          } else {
              alert("注册失败")
          }
      })
  }
  

  

});