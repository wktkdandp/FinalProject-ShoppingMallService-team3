package com.petpal.swimmer_customer.data.model

data class Items(val name: String, val mainImage: String, val price: Long, val count: Long, val option: String)
data class PaymentItems(val price: Long, val count: Long)
