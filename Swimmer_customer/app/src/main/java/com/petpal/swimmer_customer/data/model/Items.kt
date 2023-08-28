package com.petpal.swimmer_customer.data.model

import android.location.Address



data class OrderByCustomer(
    // 주문 완료 상태로 전송
    // Long -> String 변경
    var state: Long,

    // seller orderList 전달 항목
    // 데이터 자동 생성처리
    // Long -> String 변경
    // code? 에 넘겨줄 값이 없어서 일단 삭제
    // date + random 알파벳 3자리
    var orderUid: String,
    var userUid: String,

    // 보낼 수 있는 데이터
    var orderDate: String,
    var message: String,
    var payMethod: Long,
    var totalPrice: Long,

    // productUid를 통해 받아온다면 안넘겨줘도 되는 데이터?
    var itemList: List<ItemsForCustomer>,

    // 임의 처리 데이터
    // address에서 오류 발생
    var address: String,
    var couponUid: String,
    var usePoint: Long,
)