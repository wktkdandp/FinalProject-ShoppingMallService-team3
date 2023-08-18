package com.petpal.swimmer_customer.ui.product

data class Product(
    val productIdx:Long,
    val code:Long,
    val name:String,
    val price:Long,
    val mainImage:String,
    val description:String,
    val descriptionImage:String,
    val sellerIdx:Long,
    val sizeList: List<String>,
    val colorList:List<String>,
    val hashTag:String,
//    val category:Category,
//    val reviewList :List<Review>,
    val orderCount:Long,
    val regDate:String,
    val size:Long,
    val color:String
)
