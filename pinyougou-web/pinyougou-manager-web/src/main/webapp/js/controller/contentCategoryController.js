app.controller("contentCategoryController",function ($scope,$controller,baseService) {
    $controller('baseController',{$scope:$scope});

    $scope.search = function (page,rows) {
        baseService.findByPage("/contentCategory/findByPage",page,rows).then(function (value) {
            $scope.dataList = value.data.rows;
            $scope.paginationConf.totalItems = value.data.total;
        })
    }

    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }
        baseService.sendPost("/contentCategory/" + url, $scope.entity).then(function (response) {
            if (response.data) {
                $scope.reload();
            } else {
                alert("操作失败");
            }
        });
    }

    $scope.show = function (entity) {
        var newEntity = JSON.stringify(entity);
        $scope.entity = JSON.parse(newEntity);
    }

    $scope.delete = function () {
        if($scope.ids.length>0){
            baseService.sendGet("/contentCategory/delete?ids="+$scope.ids).then(function (value) {
                if (value.data) {
                    $scope.reload();
                } else {
                    alert("操作失败");
                }
            })
        }else {
            alert("请选择");
        }

    }
})
