package com.petpal.swimmer_customer.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.Ranking


class ProductViewModel() : ViewModel() {

    private val _productDetail: MutableLiveData<List<Ranking>> = MutableLiveData()
    private var _productDetailImage: MutableLiveData<String> = MutableLiveData()
    private var _rankingBrand: MutableLiveData<String> = MutableLiveData()
    private var _rankingTitle: MutableLiveData<String> = MutableLiveData()
    private var _rankingPrice: MutableLiveData<String> = MutableLiveData()

    val productDetail: LiveData<List<Ranking>>
        get() = _productDetail

    val productDetailImage: LiveData<String>
        get() = _productDetailImage

    val rankingBrand: LiveData<String>
        get() = _rankingBrand
    val rankingTitle: LiveData<String>
        get() = _rankingTitle

    val rankingPrice: LiveData<String>
        get() = _rankingPrice

    fun setProductDetailRanking(exList: List<Ranking>) {
        _productDetail.value = exList
    }

    fun productDetailImageUri(imageUrl: String) {
        _productDetailImage.value = imageUrl
    }

    fun rankingText(brandText: String, titleText: String, priceText: String) {
        _rankingBrand.value = brandText
        _rankingTitle.value = titleText
        _rankingPrice.value = priceText
    }


//    // 상품 목록을 가져온다.
//    fun getProductByIdx(productIdx:Long):LiveData<Product>{
//
//    }

}