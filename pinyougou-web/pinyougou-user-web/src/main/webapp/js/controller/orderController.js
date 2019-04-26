app.controller('orderController', function($scope,$controller, baseService) {
    $controller('baseController', {$scope: $scope});


    $scope.showOrder = function () {
        baseService.sendGet("order/showOrder").then(function (response) {
            $scope.data = response.data;
            alert($scope.data.orderItem)
        })
    }
})