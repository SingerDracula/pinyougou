/** 定义搜索控制器 */
app.controller("searchController" ,function ($scope,$sce,$location,$http, baseService) {

   $scope.searchParam = {keywords:'',category:'',brand:'',price:'',spec:{},page:1,rows:20,sortField:'',sort:''};
   $scope.searchs = function () {
       baseService.sendPost("/itemSearch/search",$scope.searchParam).then(function (response) {
           $scope.resultMap = response.data;
           initPageNum();
       })
   }

   $scope.trustHtml = function (html) {
        return $sce.trustAsHtml(html);
   }

   $scope.addItemSearch = function (key,value) {

        if(key == 'category' || key == 'brand' || key == 'price'){
            $scope.searchParam[key] = value;
        }else{
            $scope.searchParam.spec[key] = value;
        }
       $scope.searchParam.page=1;
       $scope.searchs();
   }
   
   $scope.removeSearchItem = function (key) {
       if(key == 'category' || key == 'brand' || key == 'price'){
           $scope.searchParam[key] = '';
       }else{
           delete $scope.searchParam.spec[key];
       }
       $scope.searchParam.page=1;
       $scope.searchs();
   }
   
   var initPageNum = function () {
       $scope.pageNums = [];
       var totalPage = $scope.resultMap.totalPage;
       var firstPage = 1;
       var lastPage = totalPage;
       $scope.firstDot = true;
       $scope.lastDot = true;
       if(totalPage>5){
           if($scope.searchParam.page<=3){
               lastPage = 5;
               $scope.firstDot = false;
           }
           else if($scope.searchParam.page>totalPage-3){
                firstPage = totalPage - 4;
               $scope.lastDot = false;
           }else{
               firstPage = $scope.searchParam.page - 2;
               lastPage = $scope.searchParam.page + 2;
           }
       }else{
           $scope.firstDot = false;
           $scope.lastDot = false;
       }


       for(var i = firstPage;i<=lastPage;i++){

           $scope.pageNums.push(i);
       }
   }
   
   $scope.pageSearch = function (page) {
       page = parseInt(page);
       if(page>=1 && page<=$scope.resultMap.totalPage && page != $scope.searchParam.page){
           $scope.searchParam.page = page;
           $scope.searchs();
       }
   }

   $scope.sortSearch = function (sort,sortField) {
       $scope.searchParam.sort = sort;
       $scope.searchParam.sortField = sortField;
       $scope.searchParam.page=1;
       $scope.searchs();
   }
   
   $scope.getKeywords = function () {
       $scope.searchParam.keywords = $location.search().keywords;
       $scope.searchs();
   }

    $scope.loadUserName = function () {
        $scope.redirectUrl = window.encodeURIComponent(location.href);
        $http.get("/user/showName").then(function (response) {
            $scope.loginName = response.data.loginName;
        })
    }
});
