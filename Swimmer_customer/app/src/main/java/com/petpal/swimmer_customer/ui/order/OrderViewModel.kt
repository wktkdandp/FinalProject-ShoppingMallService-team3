package com.petpal.swimmer_customer.ui.order

import android.net.Uri
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

    private val _customer = MutableLiveData<Customer>()
    val customer: LiveData<Customer> = _customer

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

    fun setOrderWithIdx(orderIdx: Int) {
        _order.postValue(orderList.value!![orderIdx])
    }

    fun setOrder(order: Order) {
        _order.postValue(order)
    }

    fun getCustomerByUid(uid: String) {
        orderRepository.getCustomerByUid(uid) {
            _customer.postValue(it)
        }
    }


    fun fetchImageDataForRecyclerView(
        imagePath: String,
        onSuccess: (Uri) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        orderRepository.getImageData(
            imagePath,
            onSuccess = { imageData ->
                onSuccess(imageData)
            },
            onError = { exception ->
                onError(exception)
            }
        )

    }
}

data class Customer(
    var email: String,
    var name: String,
    var contact: String,
)