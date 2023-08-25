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

    // 주문 상태별 건 수
    private var _paymentCount = MutableLiveData<Long>()
    var paymentCount: LiveData<Long> = _paymentCount

    private var _readyCount = MutableLiveData<Long>()
    var readyCount: LiveData<Long> = _readyCount

    private var _processCount = MutableLiveData<Long>()
    var processCount: LiveData<Long> = _processCount

    private var _completeCount = MutableLiveData<Long>()
    var completeCount: LiveData<Long> = _completeCount

    private var _cancelCount = MutableLiveData<Long>()
    var cancelCount: LiveData<Long> = _cancelCount

    private var _exchangeCount = MutableLiveData<Long>()
    var exchangeCount: LiveData<Long> = _exchangeCount

    private var _refundCount = MutableLiveData<Long>()
    var refundCount: LiveData<Long> = _refundCount

    init {
        _orderList.value = mutableListOf()
        _paymentCount.value = 0L
        _paymentCount.value = 0L
        _readyCount.value = 0L
        _processCount.value = 0L
        _completeCount.value = 0L
        _cancelCount.value = 0L
        _exchangeCount.value = 0L
        _refundCount.value = 0L
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

    // 주문상태별 주문 건 수 카운팅
    fun getOrderCountByState(sellerUid: String) {
        _readyCount.value = orderList.value?.count { it.state == OrderState.READY.code}?.toLong()
        _processCount.value = orderList.value?.count { it.state == OrderState.PROCESS.code }?.toLong()
        _completeCount.value = orderList.value?.count { it.state == OrderState.COMPLETE.code }?.toLong()

        _cancelCount.value = orderList.value?.count { it.state == OrderState.CANCEL.code }?.toLong()
        _exchangeCount.value = orderList.value?.count { it.state == OrderState.EXCHANGE.code}?.toLong()
        _refundCount.value = orderList.value?.count { it.state == OrderState.REFUND.code }?.toLong()
    }
}

// 주문상태 코드
enum class OrderState(val code: Long, val str: String){
    PAYMENT(1, "결제완료"),
    READY(2, "배송준비"),
    PROCESS(3, "배송중"),
    COMPLETE(4, "배송완료"),
    CANCEL(5, "취소"),
    EXCHANGE(6, "교환"),
    REFUND(7, "환불")
}