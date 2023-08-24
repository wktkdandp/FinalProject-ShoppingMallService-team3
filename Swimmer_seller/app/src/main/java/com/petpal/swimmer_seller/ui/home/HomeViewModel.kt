package com.petpal.swimmer_seller.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Item
import com.petpal.swimmer_seller.data.model.Order
import com.petpal.swimmer_seller.data.repository.OrderRepository
import com.petpal.swimmer_seller.data.repository.ProductRepository

class HomeViewModel(private val productRepository: ProductRepository, private val orderRepository: OrderRepository) : ViewModel() {
    // 로그인 판매자에게 온 주문 목록
    var loginSellerOrderList = MutableLiveData<MutableList<Order>>()

    // 주문 상태 건 수
    var paymentCount = MutableLiveData<Long>()
    var readyCount = MutableLiveData<Long>()
    var deliveryCount = MutableLiveData<Long>()
    var completeCount = MutableLiveData<Long>()
    var cancelCount = MutableLiveData<Long>()
    var exchangeCount = MutableLiveData<Long>()
    var refundCount = MutableLiveData<Long>()

    init {
        paymentCount.value = 0L
        readyCount.value = 0L
        deliveryCount.value = 0L
        completeCount.value = 0L
        cancelCount.value = 0L
        exchangeCount.value = 0L
        refundCount.value = 0L
    }

    // 등록 상품 건 수
    var productCount = MutableLiveData<Long>()

    // 로그인 판매자가 등록한 상품 건 수 가져오기
    fun getProductCount(sellerUid: String){
        productRepository.getProductListBySellerIdx(sellerUid){
            productCount.value = it.result.childrenCount
        }
    }

    // 로그인 판매자에게 들어온 주문 상태별 건 수 가져오기
    fun getOrderBySellerUid(loginSellerUid: String) {
        orderRepository.getAllOrder { task ->
            val orderList = mutableListOf<Order>()

            if (task.result.exists()) {
                // 로그인 판매자가 포함된 주문 여부
                var isSellerOrder = false

                for (orderSnapshot in task.result.children) {
                    val itemListSnapshot = orderSnapshot.child("itemList")
                    val itemList = mutableListOf<Item>()
                    for (itemSnapshot in itemListSnapshot.children) {
                        val sellerUid = itemSnapshot.child("sellerUid").value as String
                        if (sellerUid == loginSellerUid) {
                            isSellerOrder = true
                        }
                        val productUid = itemSnapshot.child("productUid") as String
                        val size = itemSnapshot.child("size") as String
                        val color = itemSnapshot.child("color") as String
                        val quantity = itemSnapshot.child("quantity") as Long
                        val name = itemSnapshot.child("name") as String
                        val mainImage = itemSnapshot.child("mainImage") as String
                        val price = itemSnapshot.child("price") as Long
                        val item = Item(productUid, size, color, quantity, sellerUid, name, mainImage, price)

                        itemList.add(item)
                    }

                    if (isSellerOrder) {
                        val orderUid = orderSnapshot.child("orderUid").value as String
                        val orderDate = orderSnapshot.child("orderDate") as String
                        val message = orderSnapshot.child("message") as String
                        val state = orderSnapshot.child("state") as String
                        val couponUid = orderSnapshot.child("couponUid") as String
                        val usePoint = orderSnapshot.child("userPoint") as Long
                        val payMethod = orderSnapshot.child("couponUid") as String
                        val totalPrice = orderSnapshot.child("totalPrice") as Long
                        val address = orderSnapshot.child("address") as String

                        val order = Order(orderUid, orderDate, message, state, address
                            , itemList, couponUid, usePoint, payMethod, totalPrice)
                        orderList.add(order)
                    }
                }

                loginSellerOrderList.value = orderList
            }
        }
    }

    // 주문상태별 주문 건수 구하기
    fun getOrderCountByState(sellerUid: String) {
        getOrderBySellerUid(sellerUid)
        
        val orderList = loginSellerOrderList.value!!

        // 주문상태 주문 건 수
        paymentCount.value = orderList.count { it.state == OrderState.PAYMENT.str }.toLong()
        readyCount.value = orderList.count { it.state == OrderState.READY.str}.toLong()
        deliveryCount.value = orderList.count { it.state == OrderState.PROCESS.str }.toLong()
        completeCount.value = orderList.count { it.state == OrderState.COMPLETE.str }.toLong()

        cancelCount.value = orderList.count { it.state == OrderState.CANCEL.str }.toLong()
        exchangeCount.value = orderList.count { it.state == OrderState.EXCHANGE.str}.toLong()
        refundCount.value = orderList.count { it.state == OrderState.REFUND.str }.toLong()
    }

    /*
    // orderSnapshot.getValue(Order::class.java) 코드로 객체 바로 매핑하는 방식인데 테스트 전까지는 잘 작동할지 몰라서 보류합니다
    fun getAllOrderCount(sellerUid: String) {
        orderRepository.getAllOrder { task ->
            val orderList = mutableListOf<Order>()
            for (orderSnapshot in task.result.children) {
                val order = orderSnapshot.getValue(Order::class.java)
                if (order != null) {
                    for (item in order.itemList) {
                        if (item.sellerUid == sellerUid) {
                            orderList.add(order)
                            break
                        }
                    }
                }
            }

            // 판매자에게 들어온 전체 주문 수
            orderCount.value = orderList.count().toLong()

            // 주문상태 주문 건 수
            paymentCount.value = orderList.count { it.state == OrderState.PAYMENT.code }.toLong()
            readyCount.value = orderList.count { it.state == OrderState.READY.code}.toLong()
            deliveryCount.value = orderList.count { it.state == OrderState.PROCESS.code }.toLong()
            completeCount.value = orderList.count { it.state == OrderState.COMPLETE.code }.toLong()

            cancelCount.value = orderList.count { it.state == OrderState.CANCEL.code }.toLong()
            exchangeCount.value = orderList.count { it.state == OrderState.EXCHANGE.code}.toLong()
            refundCount.value = orderList.count { it.state == OrderState.REFUND.code }.toLong()
        }
    }
    */
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

