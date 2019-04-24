app.controller("contentController",function ($scope,$controller,baseService) {
    $controller('baseController',{$scope:$scope});

    $scope.search = function (page,rows) {
        baseService.findByPage("/content/findByPage",page,rows).then(function (value) {
            $scope.dataList = value.data.rows;
            $scope.paginationConf.totalItems = value.data.total;
        })
    }

    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }
        baseService.sendPost("/content/" + url, $scope.entity).then(function (response) {
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
            baseService.sendGet("/content/delete?ids="+$scope.ids).then(function (value) {
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

    $scope.uploadFile = function () {
        baseService.uploadFile().then(function (value) {
            if (value.data.status == 200) {
                $scope.entity.pic = value.data.url;
            } else {
                alert("上传失败");
            }
        })
    }

    $scope.findAllCategory = function () {
        baseService.sendGet("/contentCategory/findAll").then(function (value) {
            $scope.contentCategoryList = value.data;
        })
    }

    $scope.statuss = ["无效","有效"];

    $scope.status = function ($event) {
        $scope.entity.status = $event.target.checked?1:0;
    }
})
