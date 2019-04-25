app.controller("baseController", function($scope,$controller,baseService){
    $scope.redirectUrl = window.encodeURIComponent(location.href);
})