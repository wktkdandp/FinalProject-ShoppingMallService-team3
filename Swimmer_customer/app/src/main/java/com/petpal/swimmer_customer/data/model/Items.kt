package com.petpal.swimmer_customer.data.model

import android.location.Address

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
    // Long -> String 변경
    var state: String,

    // seller orderList 전달 항목
    // 데이터 자동 생성처리
    // Long -> String 변경
    // code? 에 넘겨줄 값이 없어서 일단 삭제
    // date + random 알파벳 3자리
    var orderUid: String,

    // 보낼 수 있는 데이터
    var orderDate: String,
    var message: String,
    var payMethod: String,
    var totalPrice: Long,

    // productUid를 통해 받아온다면 안넘겨줘도 되는 데이터?
    var itemList: List<ItemsForCustomer>,

    // 임의 처리 데이터
    // address에서 오류 발생
    // var address: Address,
    var couponUid: String,
    var usePoint: Long,
)