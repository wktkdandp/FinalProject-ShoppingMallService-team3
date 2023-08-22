package com.petpal.swimmer_seller.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petpal.swimmer_seller.data.model.Address
import com.petpal.swimmer_seller.data.model.Item
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

                /*
                // data class라서 위 방식 안통하면 아래 방식으로 대체 가능
                val orderUid = orderSnapshot.child("orderUid").value as String
                val code = orderSnapshot.child("code").value as Long
                val orderDate = orderSnapshot.child("orderDate").value as String
                val message = orderSnapshot.child("message").value as String
                val state = orderSnapshot.child("state").value as Long
                val address = orderSnapshot.child("address").value as Address
                val itemList = mutableListOf<Item>()
                for (itemSnapshot in orderSnapshot.child("itemList").children) {
                    val productUid = itemSnapshot.child("productUid").value as String
                    val size = itemSnapshot.child("size").value as Long
                    val color = itemSnapshot.child("color").value as Long
                    val quantity = itemSnapshot.child("quantity").value as Long
                    val sellerUid = itemSnapshot.child("sellerUid").value as String

                    val item = Item(productUid, size, color, quantity, sellerUid)
                    itemList.add(item)
                }
                val couponUid = orderSnapshot.child("couponUid").value as String
                val userPoint = orderSnapshot.child("usePoint").value as Long
                val payMethod = orderSnapshot.child("payMethod").value as Long
                val totalPrice = orderSnapshot.child("totalPrice").value as Long

                val order = Order(orderUid, code, orderDate, message,
                    state, address, itemList, couponUid, userPoint, payMethod, totalPrice)

                orderList.add(order)
                 */
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
enum class OrderState(val code: Long, val str: String){
    PAYMENT(1, "결제완료"),
    READY(2, "배송준비"),
    DELIVERY(3, "배송중"),
    COMPLETE(4, "배송완료"),
    CANCEL(5, "취소"),
    EXCHANGE(6, "교환"),
    REFUND(7, "환불")
}

