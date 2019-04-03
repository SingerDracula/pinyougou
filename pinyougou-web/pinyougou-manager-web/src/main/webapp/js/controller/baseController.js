app.controller("baseController",function ($scope){
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 0,
        perPageOptions: [10, 20, 30],
        onChange: function () {
            $scope.reload();
        }
    };

    $scope.reload = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    $scope.ids = [];
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            $scope.ids.push(id);
        } else {
            var idx = $scope.ids.indexOf(id);
            $scope.ids.splice(idx, 1);
        }
    }

    $scope.jsonArr2Str = function(jsonArrStr, key){
        var jsonArr = JSON.parse(jsonArrStr);
        var resArr = [];
        for (var i = 0; i < jsonArr.length; i++){
            var json = jsonArr[i];
            resArr.push(json[key]);
        }
        return resArr.join(",");
    };

})