package com.petpal.swimmer_seller.data.model

// 판매자 클래스
data class Seller(
    var email: String,
    var businessRegNumber: String,
    var representName: String,
    var brandName: String,
    var description: String,
    var address: Address,
    var brandPhoneNumber: String,
    var bankName: String,
    var accountNumber: String,
    var contactName: String,
    var contactPhoneNumber: String,
    var contactEmail: String
)