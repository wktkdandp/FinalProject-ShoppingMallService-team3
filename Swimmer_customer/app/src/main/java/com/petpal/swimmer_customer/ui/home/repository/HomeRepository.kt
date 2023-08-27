package com.petpal.swimmer_customer.ui.home.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.petpal.swimmer_customer.data.model.Category
import com.petpal.swimmer_customer.data.model.Product
import kotlinx.coroutines.tasks.await

class HomeRepository {
    private val _productListLiveData = MutableLiveData<List<Product>>()
     val productListLiveData:LiveData<List<Product>>
        get() = _productListLiveData
    private val productList: MutableList<Product> = mutableListOf()


    suspend fun fetchProductDataFromFirebase(): List<Product> {

        val hashTagList: MutableList<String> = mutableListOf()
        val mainImageList: MutableList<String> = mutableListOf()
        val colorList: MutableList<String> = mutableListOf()
        val sizeList: MutableList<String> = mutableListOf()

        // firebase 객체를 생성한다.
        val database = FirebaseDatabase.getInstance()
        // TestData에 접근한다.
        val testDataRef = database.getReference("products")

        // 전부를 가져온다.
        val dataSnapshot = testDataRef.get().await()

        // 가져온 데이터의 수 만큼 반복한다.
        for (a1 in dataSnapshot.children) {
            // 데이터를 가져온다.
            val categoryMain = a1.child("category").child("main").value
            val categoryMid = a1.child("category").child("mid").value
            val categorySub = a1.child("category").child("sub").value
            val code = a1.child("code").value
            val description = a1.child("description").value
            val descriptionImage = a1.child("descriptionImage").value
            val hashTag = a1.child("hashTag").value
            val mainImage = a1.child("mainImage").value
            val name = a1.child("name").value
            val orderCounter = a1.child("orderCount").value
            val price = a1.child("price").value
            val productUid = a1.child("productUid").value
            val regDate = a1.child("regDate").value
            val sellerUid = a1.child("sellerUid").value
            val brandName = a1.child("brandName").value
            val color = a1.child("colorList").value
            val size = a1.child("sizeList").value

            hashTagList.add(hashTag.toString())
            mainImageList.add(mainImage.toString())
            colorList.add(color.toString())
            sizeList.add(size.toString())

            productList.add(
                Product(
                    Category(
                        categoryMain.toString(),
                        categoryMid.toString(),
                        categorySub.toString()
                    ),
                    code.toString(),
                    description.toString(),
                    descriptionImage.toString(),
                    hashTagList.toList(), // toList() 메서드로 새로운 리스트 생성
                    mainImageList.toList(),
                    name.toString(),
                    orderCounter.toString().toInt(),
                    price.toString().toInt(),
                    productUid.toString(),
                    regDate.toString(),
                    sellerUid.toString(),
                    brandName.toString(),
                    colorList.toList(),
                    sizeList.toList()
                )
            )
        }

        _productListLiveData.value = productList // LiveData에 값 설정
        return productList
    }
}