/** 定义首页控制器层 */
app.controller("indexController", function($scope,$http, baseService){

    $scope.findContentByCategoryId = function (id) {
        baseService.sendGet("/content/findContentByCategoryId?id="+id).then(function (value) {
            $scope.contentList = value.data;
        })
    }

    $scope.search = function () {
        var keyword = $scope.keyword?$scope.keyword:'';
        location.href = "http://localhost:9104?keywords="+keyword;
    }

    $scope.loadUserName = function () {
        $scope.redirectUrl = window.encodeURIComponent(location.href);
        $http.get("/user/showName").then(function (response) {
            $scope.loginName = response.data.loginName;
        })
    }
});