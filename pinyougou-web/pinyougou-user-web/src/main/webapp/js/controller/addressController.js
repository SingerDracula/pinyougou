app.controller('addressController', function($scope,$controller,baseService){
    // 指定继承baseController
    $controller('baseController', {$scope:$scope});

    $scope.a = {notes:[]};
    $scope.showAddress = function () {
        baseService.sendGet("address/findAddress").then(function (response) {
            $scope.address = response.data;
        })
    }


    $scope.show = function (entity) {
        var newEntity = JSON.stringify(entity);
        $scope.a = JSON.parse(newEntity);
    }

    $scope.save = function () {
        baseService.sendPost("/address/save",$scope.a).then(function (response) {
            if(response.data){
                location.href = "home-setting-address.html";
            }else {
                alert("修改失败");
            }
        })
    }

    $scope.add = function (value) {
        $scope.a.notes = value;
    }

    $scope.delete = function (id) {
        baseService.sendGet("/address/delete?id="+id).then(function (response) {
            if(response.data){
                location.href = "home-setting-address.html";
            }else {
                alert("删除失败");
            }
        })
    }

    $scope.modity = function (id) {
        baseService.sendGet("/address/modity?id="+id).then(function (response) {
            if(response.data){
                location.href = "home-setting-address.html";
            }else {
                alert("设置失败");
            }
        })
    }

})