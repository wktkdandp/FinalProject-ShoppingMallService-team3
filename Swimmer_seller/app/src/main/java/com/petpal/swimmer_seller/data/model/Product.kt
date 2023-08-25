package com.petpal.swimmer_seller.data.model

import android.graphics.Bitmap
import android.net.Uri

// 상품 클래스
data class Product(
    var productUid: String,
    var code: String,
    var name: String,
    var price: Long,
    var mainImage: List<String>,
    var description: String,
    var descriptionImage: String,
    var sellerUid: String,
    var sizeList: List<Long>,
    var colorList: List<Long>,
    var hashTag: List<String>,
    var category: Category,
    var reviewList: List<Review>,
    var orderCount: Long,
    var regDate: String
)

data class Image(
    var uri: Uri,
    var bitmap: Bitmap,
    var fileName: String
)