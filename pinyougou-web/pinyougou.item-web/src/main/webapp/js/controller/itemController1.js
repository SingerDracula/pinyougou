app.controller('itemController1',function ($scope,$http) {
    $scope.addNum = function (x) {
        x = parseInt(x);
        $scope.num += x;
        if($scope.num <1 ){
            $scope.num = 1;
        }
    }

    $scope.specItems = {};
    $scope.selectSpec = function (name,value) {
        $scope.specItems[name] = value;
        $scope.searchSku();
    }

    $scope.isSelect = function (name,value) {
        return $scope.specItems[name] == value;
    }

    $scope.loadSku = function () {
        $scope.sku = itemList[0];
        $scope.specItems = JSON.parse($scope.sku.spec);
    }

    $scope.searchSku = function () {
        for(var i = 0;i<itemList.length;i++){
            if(itemList[i].spec == JSON.stringify($scope.specItems)){
                $scope.sku = itemList[i];
                return;
            }
        }
    }
    
    $scope.addCart = function () {
        $http.get("http://cart.pinyougou.com/cart/addCart?itemId="+$scope.sku.id+"&num="+$scope.num,{"withCredentials":true}).then(function (response) {
            if (response.data){
                location.href = "http://cart.pinyougou.com/cart.html";
            }else {
                alert("添加失败");
            }
        })
    }

    $scope.loadUserName = function () {
        $scope.redirectUrl = window.encodeURIComponent(location.href);
        $http.get("/user/showName").then(function (response) {
            $scope.loginName = response.data.loginName;
        })
    }
})