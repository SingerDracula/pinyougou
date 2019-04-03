app.controller("itemCatController",function ($scope,$controller,baseService) {
    $controller("baseController",{$scope:$scope})

    $scope.findItemCatByParentId = function (id) {
        baseService.sendGet("/itemCat/finaItemCatByParentId?parentId="+id).then(function (value) {
            $scope.dataList = value.data;
        })
    }

    $scope.grade = 1;

    $scope.selectList = function (entity,grade) {
        $scope.grade = grade;
        if(grade == 1){
            $scope.parentId =0;
            $scope.itemCat_1 = null;
            $scope.itemCat_2 = null;
        }

        if(grade == 2){
            $scope.parentId = entity.id;
            $scope.itemCat_1 = entity;
            $scope.itemCat_2 = null;
        }

        if(grade == 3){
            $scope.parentId = entity.id;
            $scope.itemCat_2 = entity;
        }

        $scope.findItemCatByParentId(entity.id);
    }

    $scope.itemCatList = {};
    $scope.itemCatList = function(){
        baseService.sendGet("/itemCat/findAllTypeTemplate").then(function (value) {
           $scope.itemCatList = {data:value.data};
        })
    }

    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }

        $scope.entity.parentId = $scope.parentId;
        $scope.entity.typeId = $scope.entity.typeId.id
        baseService.sendPost("/itemCat/" + url, $scope.entity).then(function (response) {
            if (response.data) {
                $scope.findItemCatByParentId($scope.parentId);
            } else {
                alert("操作失败");
            }
        });
    }

    $scope.show = function (entity) {
        var newEntity = JSON.stringify(entity);
        $scope.entity = JSON.parse(newEntity);

        $scope.findTypeOneById(entity.typeId);

    }

    $scope.findTypeOneById = function (id) {
        baseService.sendGet("/itemCat/findTypeOneById?id="+id).then(function (value) {
            $scope.entity.typeId = value.data;
        })
    }

    $scope.delete = function () {
        baseService.deleteById("/itemCat/delete",$scope.ids).then(function (value) {
            if (value.data) {
                $scope.findItemCatByParentId($scope.parentId);
            } else {
                alert("操作失败");
            }
        })
    }

})