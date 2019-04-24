app.controller('cartController', function($scope,$controller,baseService){
    // 指定继承baseController
    $controller('baseController', {$scope:$scope});

    $scope.findCarts = function () {
        baseService.sendGet("/cart/findCart").then(function (response) {
            $scope.cartList = response.data;
            $scope.totalEntity = {totalNum:0,totalMoney:0.00};
            for(var i = 0;i<response.data.length;i++){
                var cart = response.data[i];
                for(var j = 0;j<cart.orderItems.length;j++){
                    var orderItem = cart.orderItems[j];
                    $scope.totalEntity.totalNum += orderItem.num;
                    $scope.totalEntity.totalMoney += orderItem.totalFee;
                }
            }
        })
    }

    $scope.addCart = function (itemId,num) {
        baseService.sendGet("/cart/addCart?itemId="+itemId+"&num="+num).then(function (response) {
            if(response){
                $scope.findCarts();
            }else {
                alert("操作失败");
            }
        })
    }
});
