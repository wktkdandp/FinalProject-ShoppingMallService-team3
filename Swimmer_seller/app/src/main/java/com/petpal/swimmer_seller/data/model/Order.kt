package com.petpal.swimmer_seller.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

// orderSnapshot.getValue(Order::class.java) 코드로 객체 바로 매핑되게 설정
// TODO 테스트 후 불안정하다 싶으면 게시판 프로젝트처럼 값 하나씩 매핑하는 방식으로 바꿀 예정
@IgnoreExtraProperties
data class Order(
    var orderUid:String = "",
    var code:String = "",
    var orderDate:String = "",
    var message:String = "",
    var state:Long = 0L,
    var address:Address = Address(0L, "", "", ""),
    var itemList:List<Item> = listOf(),
    var couponUid:String = "",
    var usePoint:Long = 0L,
    var payMethod:Long = 0L,
    var totalPrice:Long = 0L
) {
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "orderUid" to orderUid,
            "code" to code,
            "orderDate" to orderDate,
            "message" to message,
            "state" to state,
            "address" to address,
            "itemList" to itemList,
            "couponUid" to couponUid,
            "usePoint" to usePoint,
            "payMethod" to payMethod,
            "totalPrice" to totalPrice
        )
    }
}

