app.controller("brandController",function ($scope,$controller,baseService) {
    $controller('baseController',{$scope:$scope});

    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }
        baseService.sendPost("/brand/" + url, $scope.entity).then(function (response) {
            if (response.data) {
                $scope.reload();
            } else {
                alert("操作失败");
            }
        });
    }

    $scope.search = function (page, rows) {
        baseService.findByPage("/brand/findByPage", page, rows, $scope.searchEntity).then(function (response) {
            $scope.dataList = response.data.rows;
            $scope.paginationConf.totalItems = response.data.total;
        })
    }

    $scope.show = function (entity) {
        var newEntity = JSON.stringify(entity);
        $scope.entity = JSON.parse(newEntity);
    }

    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/brand/delete", $scope.ids).then(function (response) {
                if (response.data) {
                    $scope.reload();
                }
            })
        } else {
            alert('请选择要删除的品牌!!!!!');
        }
    }

})