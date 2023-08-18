package com.petpal.swimmer_seller.data.model

// 주소 클래스
data class Address(
    var addressIdx: Long,
    var postCode: Long,
    var address: String,
    var name: String,
    var phoneNumber: String
)