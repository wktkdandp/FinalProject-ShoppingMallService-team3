package com.petpal.swimmer_customer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.ProductDetailModel


class HomeFragmentViewModel  : ViewModel() {

    private val _productDetailList: MutableLiveData<List<ProductDetailModel>> = MutableLiveData()


    val productDetailList: LiveData<List<ProductDetailModel>>
        get() = _productDetailList


    fun setProductDetail(list: List<ProductDetailModel>) {
        _productDetailList.value = list
    }

}