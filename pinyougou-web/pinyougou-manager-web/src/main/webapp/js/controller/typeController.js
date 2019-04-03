app.controller("typeController",function ($scope,$controller,baseService) {
    $controller("baseController",{$scope:$scope});
    $scope.search = function (page,rows) {
        baseService.findByPage("/typeTemplate/findByPage",page,rows,$scope.searchEntity).then(function (value) {
            $scope.dataList = value.data.rows;
            $scope.paginationConf.totalItems = value.data.total;
        })
    }

    $scope.brandList = {};

    $scope.findBrandList = function () {
        baseService.sendGet("/typeTemplate/findBrandList").then(function (value) {
            $scope.brandList = {data:value.data};
        })
    }

    $scope.specList = {};
    $scope.specList = function () {
        baseService.sendGet("/typeTemplate/findSpecList").then(function (value) {
            $scope.specList = {data:value.data};
        })
    }

    $scope.addTableRow = function () {
        $scope.entity.customAttributeItems.push({});
    }
    
    $scope.deleteTableRow = function (index) {
        $scope.entity.customAttributeItems.splice(index,1);
    }

    $scope.saveOrUpdate = function () {
        var url = "save";
        if($scope.entity.id){
            url = "update";
        }
        baseService.sendPost("/typeTemplate/"+url,$scope.entity).then(function (value) {
            if(value.data){
                $scope.reload();
            }else{
                alert("操作失败");
            }
        })

    }

    $scope.show = function (entity) {
        var newEntity = JSON.stringify(entity);
        $scope.entity = JSON.parse(newEntity);

        $scope.entity.brandIds = JSON.parse(entity.brandIds);
        $scope.entity.specIds = JSON.parse(entity.specIds);
        $scope.entity.customAttributeItems = JSON.parse(entity.customAttributeItems);
    }

    $scope.delete = function(){
        baseService.deleteById("/typeTemplate/delete",$scope.ids).then(function (value) {
            if(value.data){
                $scope.reload();
            }else {
                alert("操作失败");
            }
        })
    }

})