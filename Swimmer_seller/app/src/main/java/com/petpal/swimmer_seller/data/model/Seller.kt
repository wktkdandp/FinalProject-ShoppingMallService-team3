package com.petpal.swimmer_seller.data.model

data class Seller(
    val email:String,
    var sellerAuthUid: String,
    val businessRegNumber:String,
    val representName: String,
    val brandName: String,
    val description: String,
    val address:String,
    val brandPhoneNumber: String,
    val bankName: String,
    val accountNumber: String,
    val contactName: String,
    val contactPhoneNumber: String,
    val contactEmail: String
)