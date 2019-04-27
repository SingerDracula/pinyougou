app.controller('orderController', function($scope,$controller,$location, baseService) {
    $controller('baseController', {$scope: $scope});


    $scope.showOrder = function () {
        baseService.sendGet("order/showOrder").then(function (response) {
            $scope.data = response.data;
        })
    }

    $scope.add = function (value) {
        if(value == 1){
            return '待付款';
        }else if(value == 2){
            return '已付款';
        }
        else if(value == 2){
            return '未发货';
        }
        else if(value == 2){
            return '交易成功';
        }else if(value == 2){
            return '交易关闭';
        }
        else if(value == 2){
            return '待评价';
        }

    }

    $scope.pay = function (money,orderId) {
        baseService.sendGet("order/pay?totalMoney="+money+"&orderId="+orderId).then(function (response) {

            $scope.money = (response.data.totalFee/1).toFixed(2);
            $scope.outTradeNo = response.data.outTradeNo;
            $scope.url = response.data.codeUrl
            location.href = "pay.html?money="+$scope.money+"&outTradeNo="+$scope.outTradeNo+"&url="+$scope.url;
            var qr = new QRious({
                element : document.getElementById('qrious'),
                size : 250,
                level : 'H',
                value : response.data.codeUrl

            })
        })
    }

    $scope.showPay = function () {
        $scope.money = $location.search().money;
        $scope.outTradeNo = $location.search().outTradeNo;
        $scope.url = $location.search().url;

        var qr = new QRious({
            element : document.getElementById('qrious'),
            size : 250,
            level : 'H',
            value : $scope.url

        })
    }
})