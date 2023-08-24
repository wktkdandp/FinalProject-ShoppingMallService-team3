package com.petpal.swimmer_seller.data.model

data class Item (
    var productUid: String,
    //TODO Long으로 바꾸기
    var size: String,
    //TODO Long으로 바꾸기
    var color: String,
    var quantity: Long,
    var sellerUid: String,
    var name: String,
    var mainImage: String,
    var price: Long,
)