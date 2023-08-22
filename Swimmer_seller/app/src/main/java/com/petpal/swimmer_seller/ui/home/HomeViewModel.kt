package com.petpal.swimmer_seller.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Order
import com.petpal.swimmer_seller.data.repository.OrderRepository
import com.petpal.swimmer_seller.data.repository.ProductRepository

class HomeViewModel(private val productRepository: ProductRepository, private val orderRepository: OrderRepository) : ViewModel() {
    // 주문 상태 건 수
    var orderCount = MutableLiveData<Long>()
    var paymentCount = MutableLiveData<Long>()
    var readyCount = MutableLiveData<Long>()
    var deliveryCount = MutableLiveData<Long>()
    var completeCount = MutableLiveData<Long>()
    var cancelCount = MutableLiveData<Long>()
    var exchangeCount = MutableLiveData<Long>()
    var refundCount = MutableLiveData<Long>()

    // 등록 상품 건 수
    var productCount = MutableLiveData<Long>()

    // 로그인 판매자가 등록한 상품 건 수 가져오기
    fun getProductCount(sellerUid: String){
        productRepository.getProductListBySellerIdx(sellerUid){
            productCount.value = it.result.childrenCount
        }
    }

    // 로그인 판매자에게 들어온 주문 상태별 건 수 가져오기
    fun getAllOrderCount(sellerUid: String) {
        orderRepository.getOrderBySellerUid(sellerUid) { task ->
            val orderList = mutableListOf<Order>()

            for (orderSnapshot in task.result.children) {
                val order = orderSnapshot.getValue(Order::class.java)
                if (order != null) {
                    orderList.add(order)
                }
            }

            // 전체 주문 건 수 (값 잘 가져오는지 임시 테스트용으로 쓸 예정)
            orderCount.value = orderList.count().toLong()

            // 주문상태 주문 건 수
            paymentCount.value = orderList.count { it.state == OrderState.PAYMENT.code }.toLong()
            readyCount.value = orderList.count { it.state == OrderState.READY.code}.toLong()
            deliveryCount.value = orderList.count { it.state == OrderState.DELIVERY.code }.toLong()
            completeCount.value = orderList.count { it.state == OrderState.COMPLETE.code }.toLong()

            cancelCount.value = orderList.count { it.state == OrderState.CANCEL.code }.toLong()
            exchangeCount.value = orderList.count { it.state == OrderState.EXCHANGE.code}.toLong()
            refundCount.value = orderList.count { it.state == OrderState.REFUND.code }.toLong()
        }
    }
}

// 주문상태
enum class OrderState(val code: Long, name: String){
    PAYMENT(1, "결제완료"),
    READY(2, "배송준비"),
    DELIVERY(3, "배송중"),
    COMPLETE(4, "배송완료"),
    CANCEL(5, "취소"),
    EXCHANGE(6, "교환"),
    REFUND(7, "환불")
}

