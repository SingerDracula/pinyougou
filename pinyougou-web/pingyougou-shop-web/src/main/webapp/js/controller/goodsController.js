/** 定义控制器层 */
app.controller('goodsController', function($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function (page, rows) {
        baseService.findByPage("/goods/findByPage", page,
            rows, $scope.searchEntity)
            .then(function (response) {
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function () {
        $scope.goods.introduction = editor.html();
        baseService.sendPost("/goods/save", $scope.goods)
            .then(function (response) {
                if (response.data) {
                    alert("保存成功");
                    $scope.goods = {};
                } else {
                    editor.html('');
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function (entity) {
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/goods/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        /** 重新加载数据 */
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        } else {
            alert("请选择要删除的记录！");
        }
    };

    $scope.uploadFile = function () {
        baseService.uploadFile().then(function (value) {
            if (value.data.status == 200) {
                $scope.picEntity.url = value.data.url;
            } else {
                alert("上传失败");
            }
        })
    }

    $scope.goods = {goodsDesc: {itemImages: [], specificationItems: []}}

    $scope.addPic = function () {
        $scope.goods.goodsDesc.itemImages.push($scope.picEntity);
    }

    $scope.removePic = function (index) {
        $scope.goods.goodsDesc.itemImages.splice(index, 1)
    }

    $scope.findByParentId = function (parentId, name) {
        baseService.sendGet("/ItemCat/findByParentId?parentId=" + parentId).then(function (value) {
            $scope[name] = value.data;
        })
    }

    $scope.$watch('goods.category1Id', function (newValue, oldValue) {
        if (newValue) {
            $scope.findByParentId(newValue, 'itemCategory2');
        } else {
            $scope.itemCategory2 = [];
        }
    })

    $scope.$watch('goods.category2Id', function (newValue, oldValue) {
        if (newValue) {
            $scope.findByParentId(newValue, 'itemCategory3');
        } else {
            $scope.itemCategory3 = [];
        }
    })

    $scope.$watch('goods.category3Id', function (newValue, oldValue) {
        if (newValue) {
            for (var i = 0; i < $scope.itemCategory3.length; i++) {
                if (newValue == $scope.itemCategory3[i].id) {
                    $scope.goods.typeTemplateId = $scope.itemCategory3[i].typeId;
                    break;
                }
            }
        }
    })

    $scope.$watch('goods.typeTemplateId', function (newValue, oldValue) {
        if (!newValue) {
            return;
        } else {
            baseService.findOne("/typeTemplate/findBrandIdsByTemplateId", newValue).then(function (value) {
                $scope.brandIds = JSON.parse(value.data.brandIds);
                $scope.goods.goodsDesc.customAttributeItems = JSON.parse(value.data.customAttributeItems);
            })

            baseService.findOne("/typeTemplate/findSpecByTemplateId", newValue).then(function (value) {
                $scope.specList = value.data;
            })

        }
    })

    $scope.updateSpecAttr = function ($event, name, value) {
        var obj = $scope.searchByKey($scope.goods.goodsDesc.specificationItems, "attributeName", name);
        if (obj) {
            if ($event.target.checked) {
                obj.attributeValue.push(value);
            } else {
                obj.attributeValue.splice(obj.attributeValue.indexOf(value), 1);
                if (obj.attributeValue.length == 0) {
                    $scope.goods.goodsDesc.specificationItems.splice($scope.goods.goodsDesc.specificationItems.indexOf(obj), 1);

                }
            }

        } else {
            $scope.goods.goodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]})
        }
    }

    $scope.searchByKey = function (jsonArry, name, value) {
        for (var i = 0; i < jsonArry.length; i++) {
            if (jsonArry[i][name] == value) {
                return jsonArry[i];
            }
        }
    }

    $scope.creatItem = function () {
        $scope.goods.items = [{"spec": {}, price: 0, num: 9999, status: "0", isDefault: "0"}];
        var specItems = $scope.goods.goodsDesc.specificationItems;
        for (var i = 0; i < specItems.length; i++) {
            $scope.goods.items = swapItems($scope.goods.items, specItems[i].attributeName, specItems[i].attributeValue);
        }
    }

    var swapItems = function (items, attributeName, attributeValue) {
        var newItems = new Array();
        for (var i = 0; i < items.length; i++) {
            var item = items[i];
            for (var j = 0; j < attributeValue.length; j++) {
                var newItem = JSON.parse(JSON.stringify(item));
                newItem.spec[attributeName] = attributeValue[j];
                newItems.push(newItem);
            }
        }
        return newItems;

    }

    $scope.searchEntity = {};

    $scope.search = function (page,row) {
        baseService.findByPage("/goods/findByPage",page,row,$scope.searchEntity).then(function (value) {
            $scope.dataList = value.data.rows;
            $scope.paginationConf.totalItems = value.data.total;
        })
    }

    $scope.status = ['未审核','审核通过','审核未通过','关闭'];
})