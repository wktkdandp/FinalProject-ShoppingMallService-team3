package com.petpal.swimmer_seller.data.model

import android.os.Parcel
import android.os.Parcelable

// 상품 클래스
data class Product(
    var productUid: String? = null,
    var code: String? = null,
    var name: String? = null,
    var price: Long = 0L,
    var mainImage: List<String>? = listOf(),
    var description: String? = null,
    var descriptionImage: String? = null,
    var sellerUid: String? = null,
    var sizeList: List<String>? = listOf(),
    var colorList: List<String>? = listOf(),
    var hashTag: List<String>? = listOf(),
    var category: Category? = null,
    var reviewList: List<Review>? = listOf(),
    var orderCount: Long = 0L,
    var regDate: String? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readParcelable(Category::class.java.classLoader),
        parcel.createTypedArrayList(Review),
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productUid)
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeLong(price)
        parcel.writeStringList(mainImage)
        parcel.writeString(description)
        parcel.writeString(descriptionImage)
        parcel.writeString(sellerUid)
        parcel.writeStringList(sizeList)
        parcel.writeStringList(colorList)
        parcel.writeStringList(hashTag)
        parcel.writeParcelable(category, flags)
        parcel.writeTypedList(reviewList)
        parcel.writeLong(orderCount)
        parcel.writeString(regDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}

// Fragment 간의 전달용 이미지 정보 클래스
data class Image(
    var uriString: String? = null,
    var fileName: String? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uriString)
        parcel.writeString(fileName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}