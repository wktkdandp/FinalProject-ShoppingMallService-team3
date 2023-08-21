package com.petpal.swimmer_seller.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.repository.ProductRepository

class HomeViewModel(private val productRepository: ProductRepository) : ViewModel() {
    var productCount = MutableLiveData<Long>()

    // 로그인 판매자가 등록한 상품 건 수 가져오기
    fun getProductCount(sellerUid: String){
        productRepository.getProductListBySellerIdx(sellerUid){
            productCount.value = it.result.childrenCount
        }
    }
}