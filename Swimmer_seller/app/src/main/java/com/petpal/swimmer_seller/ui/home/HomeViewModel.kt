package com.petpal.swimmer_seller.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Order
import com.petpal.swimmer_seller.data.repository.OrderRepository
import com.petpal.swimmer_seller.data.repository.ProductRepository
import kotlin.math.log

class HomeViewModel(private val productRepository: ProductRepository, private val orderRepository: OrderRepository) : ViewModel() {
    // 로그인 판매자에게 온 주문 목록
    private var _loginSellerOrderList = MutableLiveData<List<Order>>()
    var loginSellerOrderList: LiveData<List<Order>> = _loginSellerOrderList

    // 주문 상태 건 수
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

    // 등록 상품 건 수
    private var _productCount = MutableLiveData<Long>()
    var productCount: LiveData<Long> = _productCount

    init {
        _loginSellerOrderList.value = mutableListOf()
        _paymentCount.value = 0L
        _paymentCount.value = 0L
        _readyCount.value = 0L
        _processCount.value = 0L
        _completeCount.value = 0L
        _cancelCount.value = 0L
        _exchangeCount.value = 0L
        _refundCount.value = 0L
    }

    // 로그인 판매자가 등록한 상품 건 수 가져오기
    fun getProductCount(sellerUid: String){
        productRepository.getProductListBySellerIdx(sellerUid){
            _productCount.value = it.result.childrenCount
        }
    }

    // 로그인 판매자에게 들어온 주문 상태별 건 수 가져오기
    fun getOrderBySellerUid(loginSellerUid: String) {
        orderRepository.getOrderBySellerUid(loginSellerUid){
            Log.d("haehyun", loginSellerOrderList.value?.joinToString(",")!!)
            _loginSellerOrderList.postValue(it)
        }
    }

    // 주문상태별 주문 건수 구하기
    fun getOrderCountByState(sellerUid: String) {
        getOrderBySellerUid(sellerUid)

        // 주문상태 주문 건 수
        _paymentCount.value = loginSellerOrderList.value?.count { it.state == OrderState.PAYMENT.code }?.toLong()
        _readyCount.value = loginSellerOrderList.value?.count { it.state == OrderState.READY.code}?.toLong()
        _processCount.value = loginSellerOrderList.value?.count { it.state == OrderState.PROCESS.code }?.toLong()
        _completeCount.value = loginSellerOrderList.value?.count { it.state == OrderState.COMPLETE.code }?.toLong()

        _cancelCount.value = loginSellerOrderList.value?.count { it.state == OrderState.CANCEL.code }?.toLong()
        _exchangeCount.value = loginSellerOrderList.value?.count { it.state == OrderState.EXCHANGE.code}?.toLong()
        _refundCount.value = loginSellerOrderList.value?.count { it.state == OrderState.REFUND.code }?.toLong()
    }
}

// 주문상태
enum class OrderState(val code: Long, val str: String){
    PAYMENT(1, "결제완료"),
    READY(2, "배송준비"),
    PROCESS(3, "배송중"),
    COMPLETE(4, "배송완료"),
    CANCEL(5, "취소"),
    EXCHANGE(6, "교환"),
    REFUND(7, "환불")
}

