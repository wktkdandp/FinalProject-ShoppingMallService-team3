package com.petpal.swimmer_seller.data.model


data class Order(
    var orderUid:String,
    var orderDate:String,
    var message:String,
    var state:Long,
    //TODO : 나중에 Address로 변경해야함
    var address: String,
//    var address:Address,
    var itemList:List<Item>,
    var couponUid:String,
    var usePoint:Long,
    var payMethod:Long,
    var totalPrice:Long
)

