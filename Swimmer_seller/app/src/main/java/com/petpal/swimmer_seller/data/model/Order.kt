package com.petpal.swimmer_seller.data.model

data class Order(
    var orderUid:String,
    var code:String,
    var orderDate:String,
    var message:String,
    var state:Long,
    var address:Address,
    var itemList:List<Item>,
    var couponUid:String,
    var usePoint:Long,
    var payMethod:Long,
    var totalPrice:Long
)