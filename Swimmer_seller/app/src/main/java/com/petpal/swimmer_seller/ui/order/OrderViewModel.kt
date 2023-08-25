package com.petpal.swimmer_seller.ui.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Item
import com.petpal.swimmer_seller.data.model.Order
import com.petpal.swimmer_seller.data.repository.OrderRepository

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    private val _orderList = MutableLiveData<List<Order>>()
    val orderList: LiveData<List<Order>> = _orderList

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> = _order

    init {
        _orderList.value = mutableListOf()
    }


    fun getOrderDetailByIdx(orderIdx: Long) {

    }

    fun getOrderBySellerUid(sellerUid: String) {
        orderRepository.getOrderBySellerUid(sellerUid) {
            Log.d("order 받아온 orders", it.toString())
            _orderList.postValue(it)
        }
    }


    fun setOrderWithIdx(orderIdx: Int){
        _order.postValue(orderList.value!![orderIdx])
    }
}