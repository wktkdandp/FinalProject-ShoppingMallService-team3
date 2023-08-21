package com.petpal.swimmer_customer.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.Ranking


class ProductViewModel(private val idx: Int) : ViewModel() {

    private val _productDetail: MutableLiveData<List<Ranking>> = MutableLiveData()
    private val _productDescription:MutableLiveData<List<Ranking>> = MutableLiveData()

    val productDetail:LiveData<List<Ranking>>
        get() = _productDetail

    val productDescription:LiveData<List<Ranking>>
        get() = _productDescription

    fun setProductDetailRanking(exList: List<Ranking>) {
        _productDetail.value = exList
    }

//    // 상품 목록을 가져온다.
//    fun getProductByIdx(productIdx:Long):LiveData<Product>{
//
//    }

}