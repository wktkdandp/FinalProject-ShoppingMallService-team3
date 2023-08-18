package com.petpal.swimmer_customer.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.model.ProductDetailModel
import com.petpal.swimmer_customer.model.Ranking


class ProductViewModel  : ViewModel() {

    private val _productDetailTitle: MutableLiveData<List<Ranking>> = MutableLiveData()

    val productDetail:LiveData<List<Ranking>>
        get() = _productDetailTitle

    fun setProductDetailRanking(exList: List<Ranking>) {
        _productDetailTitle.value =exList
    }

//    // 상품 목록을 가져온다.
//    fun getProductByIdx(productIdx:Long):LiveData<Product>{
//
//    }

}