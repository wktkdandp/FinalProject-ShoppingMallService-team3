package com.petpal.swimmer_seller.data.model

data class Item (
    var productUid: String,
    var size: String,
    var color: String,
    var quantity: Long,
    var sellerUid: String,
    var name: String,
    var mainImage: String,
    var price: Long,
)