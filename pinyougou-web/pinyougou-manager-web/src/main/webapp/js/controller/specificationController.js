app.controller("specificationController",function ($scope,$controller,baseService) {
    $controller("baseController",{$scope:$scope});

    $scope.search = function (page,rows) {
        baseService.findByPage("/specification/findByPage",page,rows,$scope.searchEntity).then(function (response) {
            $scope.dataList = response.data.rows;
            $scope.paginationConf.totalItems = response.data.total;
        })
    }


    $scope.addTableRow = function () {
        $scope.entity.specificationOptions.push({});
    }
    
    $scope.deleteTableRow = function (index) {
        $scope.entity.specificationOptions.splice(index,1);
    }

    $scope.updateOrSave = function () {
        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }
        baseService.sendPost("/specification/" + url, $scope.entity).then(function (response) {
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
        baseService.sendGet("/specification/findById?id="+entity.id).then(function (value) {
            $scope.entity.specificationOptions = value.data;
        })
    }

    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/specification/delete", $scope.ids).then(function (response) {
                if (response.data) {
                    $scope.reload();
                }
            })
        } else {
            alert('请选择要删除的规范!!!!!');
        }
    }

})