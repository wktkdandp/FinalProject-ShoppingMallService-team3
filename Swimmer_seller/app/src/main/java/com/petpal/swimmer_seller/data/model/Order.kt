package com.petpal.swimmer_seller.data.model

data class Order(
    var orderUid:String,
    var orderDate:String,
    var message:String,
    //TODO: 나중에 Long으로 바꾸기
    var state:String,
    //TODO : 나중에 Address로 변경해야함
    var address: String,
//    var address:Address,
    var itemList:List<Item>,
    var couponUid:String,
    var usePoint:Long,
    //TODO: 나중에 Long으로 바꾸기
    var payMethod:String,
    var totalPrice:Long
)