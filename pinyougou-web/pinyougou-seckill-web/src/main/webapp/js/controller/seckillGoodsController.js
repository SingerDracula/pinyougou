/** 定义秒杀商品控制器 */
app.controller("seckillGoodsController", function($scope,$controller,$location,$timeout,baseService){
    var timer = null;
    /** 指定继承cartController */
    $controller("baseController", {$scope:$scope});

    $scope.findSeckillGoods = function () {
        baseService.sendGet("/seckillGoods/findSeckillGoods").then(function (response) {
            $scope.seckillGoods = response.data;
        })
    }
    
    $scope.loadSeckillgoods = function () {
        var id = $location.search().id;
        baseService.sendGet("/seckillGoods/loadSeckillgoods?id="+id).then(function (response) {
            $scope.entity = response.data;
            $scope.downCount($scope.entity.endTime);
        })
    }

    $scope.downCount = function (endTime) {
        var time = new Date(endTime).getTime() - new Date().getTime();
        var seconds = Math.floor(time/1000);
        if(seconds>0){
            var minutes = Math.floor(seconds/60);
            var hours = Math.floor(minutes/60);
            var day = Math.floor(hours/24);
            var resArr = new Array();
            if(day > 0){
                resArr.push(day+"天");
            }
            if(hours > 0){
                resArr.push((hours - day*24)+":");
            }
            if(minutes > 0){
                resArr.push((minutes - hours*60)+":");
            }
            if(seconds > 0){
                resArr.push((seconds - minutes*60));
            }
            $scope.timeStr = resArr.join("");

            timer = $timeout(function () {
                $scope.downCount(endTime);
            },1000);
        }else{
            $scope.timeStr = "秒杀结束！";
        }
    }

    $scope.jump = function (id) {
        location.href = "seckill-item.html?id="+id;
    }
    
    $scope.submitOrder = function () {
        if($scope.loginName){
            baseService.sendGet("/seckillOrder/submitOrder?id="+$scope.entity.id).then(function (response) {
                if(response.data){
                    location.href = "/order/pay.html";
                }else {
                    alert("秒杀已经结束")
                    $timeout.cancel(timer);
                    $scope.timeStr = "秒杀结束！";
                    $scope.entity.stockCount = 0;
                    $("#show").text("秒杀结束");
                    $("#show").attr("disabled","true");
                }
            })
        }else {
            location.href = "http://sso.pinyougou.com/cas/?service="+$scope.redirectUrl;
        }

    }
    
});