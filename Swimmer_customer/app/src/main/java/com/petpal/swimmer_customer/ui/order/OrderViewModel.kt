package com.petpal.swimmer_customer.ui.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_customer.data.model.Order
import com.petpal.swimmer_customer.data.repository.OrderRepository

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    private val _orderList = MutableLiveData<List<Order>>()
    val orderList: LiveData<List<Order>> = _orderList

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> = _order

    init {
        _orderList.value = mutableListOf()
    }

    fun getOrderByUserUid(userUid: String) {
        orderRepository.getOrderByUserUid(userUid) {
            Log.d("order 받아온 orders", it.toString())
            _orderList.postValue(it)
        }
    }

    fun getOrderByState(userUid: String, vararg codes: Long) {
        orderRepository.getOrderByUserUid(userUid) { orderList ->
            Log.d("order 받아온 orders", orderList.toString())
            val tmp = mutableListOf<Order>()

            for (code in codes) {
                for (order in orderList) {
                    if (order.state == code) tmp.add(order)
                }
            }
            _orderList.postValue(tmp)
        }
    }

    fun setOrderWithIdx(orderIdx: Int){
        _order.postValue(orderList.value!![orderIdx])
    }

    fun setOrder(order: Order){
        _order.postValue(order)
    }
}