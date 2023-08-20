package com.petpal.swimmer_customer.data.model


data class Ranking(
    val rankingIdx: Int,
    val rankingImage: String,
    val brand: String,
    val title: String,
    val price: String,
    val description: String,
    val productDetailItemList: List<ProductDetailModel>,
    )