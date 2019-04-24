app.controller('orderController', function($scope,$controller,baseService,$interval,$location) {
    // 指定继承baseController
    $controller('baseController', {$scope: $scope});

    $scope.findAddress = function () {
        baseService.sendGet("/address/findAddress").then(function (response) {
            $scope.addressList = response.data;
            for(var i = 0;i<response.data.length;i++){
                if(response.data[i].isDefault == 1){
                    $scope.address = response.data[i];
                }
            }
        })
    }

    $scope.selectAddress = function (a) {
        $scope.address = a;
    }

    $scope.isSelectAddress = function (a) {
        return $scope.address == a;
    }

    $scope.delete = function (id) {
        baseService.sendGet("/address/delete?id="+id).then(function (response){
            if(response.data){
                alert("删除成功");
                $scope.findAddress();
            }else{
                alert("删除失败");
            }
        })
    }
    
    $scope.saveOrUpdate = function () {
        var url = 'save';
        if($scope.a.id){
             url = 'update';
        }
        baseService.sendPost("/address/"+url,$scope.a).then(function (response){
            if(response.data){
                alert("操作成功");
                $scope.findAddress();
            }else{
                alert("操作失败");
            }
        })

    }

    $scope.show = function (entity) {
        var newEntity = JSON.stringify(entity);
        $scope.a = JSON.parse(newEntity);
    }

    $scope.order = {paymentType:'1'}
    
    $scope.selectPayType = function (type) {
        $scope.order.paymentType = type;
    }

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

    $scope.saveOrder = function () {
        $scope.order.receiverAreaName = $scope.address.address;
        $scope.order.receiverMobile = $scope.address.mobile;
        $scope.order.receiver = $scope.address.contact;
        baseService.sendPost("/order/save",$scope.order).then(function (response) {
            if(response.data){
                if($scope.order.paymentType == 1){
                    location.href = '/order/pay.html';
                }else{
                    location.href = '/order/paysuccess.html';
                }
            }else {
                alert("提交失败");
            }
        })
    }
    
    $scope.getPayCode = function () {
        baseService.sendGet("/order/getPayCode").then(function (response) {
            $scope.money = (response.data.totalFee/100).toFixed(2);
            $scope.outTradeNo = response.data.outTradeNo;
            var qr = new QRious({
                element : document.getElementById('qrious'),
                size : 250,
                level : 'H',
                value : response.data.codeUrl

            })

            var timer = $interval(function () {
                baseService.sendGet("/order/getPayStatus?outTradeNo="+$scope.outTradeNo).then(function (response) {
                    if(response.data.status == 1){
                        $interval.cancel(timer);
                        location.href = "/order/paysuccess.html?money="+$scope.money;
                    }else if(response.data.status == 3){
                        $interval.cancel(timer);
                        location.href = "/order/payfail.html";
                    }
                })
            },3000,60);

            timer.then = function () {
                alert("二维码超时");
            }
        })
    }

    $scope.getMoney = function(){
        return $location.search().money;
    };



})