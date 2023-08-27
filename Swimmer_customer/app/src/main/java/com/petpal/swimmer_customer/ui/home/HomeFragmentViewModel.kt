package com.petpal.swimmer_customer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petpal.swimmer_customer.data.model.Product
import com.petpal.swimmer_customer.data.model.ProductDetailModel
import com.petpal.swimmer_customer.ui.home.repository.HomeRepository
import kotlinx.coroutines.launch


class HomeFragmentViewModel  : ViewModel() {

    private val _homeFragmentItemList: MutableLiveData<List<ProductDetailModel>> = MutableLiveData()
    val homeFragmentItemList: LiveData<List<ProductDetailModel>>
        get() = _homeFragmentItemList
    private val repository = HomeRepository()
    var productListLiveData: LiveData<List<Product>> = repository.productListLiveData

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val itemList = mutableListOf<ProductDetailModel>()
        for (i in 1..5) {
            itemList.add(ProductDetailModel("bannerImage$i.jpg"))
        }
        _homeFragmentItemList.postValue(itemList)
    }

    suspend fun fetchData() {
       repository.fetchProductDataFromFirebase()
    }

}