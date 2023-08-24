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

    init {
        _orderList.value = mutableListOf()
    }


    fun getOrderDetailByIdx(orderIdx:Long) {

    }

    fun getOrderBySellerUid(sellerUid: String) {
        orderRepository.getOrderBySellerUid(sellerUid) {
            Log.d("order 받아온 orders", it.toString())
            _orderList.postValue(it)
        }
    }

    fun calculateTotalPrice(itemList: List<Item>): Long {
        var totalPrice = 0L

        for (item in itemList) {
            totalPrice += item.price * item.quantity
        }

        return totalPrice
    }
}