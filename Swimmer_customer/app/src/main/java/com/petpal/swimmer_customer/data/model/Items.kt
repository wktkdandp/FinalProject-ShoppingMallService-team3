package com.petpal.swimmer_customer.data.model

import android.location.Address

data class Items(val name: String, val mainImage: String, val price: Long, val count: Long, val option: String)
data class ItemsForCustomer (
    // String 으로 test 진행 후 static 처리로 long 타입 변환 예정
    val productUid: String,
    val sellerUid: String,
    val name: String,
    val mainImage: String,
    val price: Long,

    // 임의 처리 필요한 데이터
    val quantity: Long,
    val size: String,
    val color: String,
)
data class OrderByCustomer(
    // 주문 완료 상태로 전송
    var state: Long,

    // seller orderList 전달 항목
    // 데이터 자동 생성처리
    var orderUid: Long,
    var code: Long,

    // 보낼 수 있는 데이터
    var orderDate:String,
    var message:String,
    var payMethod: Long,
    var totalPrice: Long,
    var itemList: List<ItemsForCustomer>,

    // 임의 처리 데이터
    var address: Address,
    var couponUid: Long,
    var usePoint: Long,
)