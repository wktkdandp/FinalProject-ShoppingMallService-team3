package com.petpal.swimmer_seller.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.petpal.swimmer_seller.data.model.Order

class OrderRepository {
    private val ordersRef = FirebaseDatabase.getInstance().getReference("orders")

    // 전체 주문 가져오기
    fun getAllOrder(callback: (Task<DataSnapshot>) -> Unit){
        ordersRef.orderByChild("orderUid").get().addOnCompleteListener(callback)
    }

    // 특정 주문 가져오기
    fun getOrderByOrderUid(orderUid: Long, callback: (Task<DataSnapshot>) -> Unit) {
        ordersRef.orderByChild("orderUid").equalTo(orderUid.toDouble()).get().addOnCompleteListener(callback)
    }

    /*
    // RealtimeDatabase에서 아래 쿼리가 안먹혀서 전체 읽어오고 필터링 하는 식으로 구현
    // 판매자에게 들어온 모든 상태 주문 가져오기
    fun getOrderBySellerUid(sellerUid: String, callback: (Task<DataSnapshot>) -> Unit) {
        ordersRef.orderByChild("itemList/sellerUid").equalTo(sellerUid).get().addOnCompleteListener(callback)
    }

    // 판매자에게 들어온 특정 상태 주문 가져오기
    fun getOrderBySellerUid(sellerUid: String, state: Long, callback: (Task<DataSnapshot>) -> Unit) {
        ordersRef.orderByChild("itemList/sellerUid").equalTo(sellerUid)
            .ref.orderByChild("state").equalTo(state.toDouble()).get().addOnCompleteListener(callback)
    }
    */

    // 주문 수정
    fun modifyOrder(order: Order, callback: (Task<DataSnapshot>) -> Unit) {
        ordersRef.orderByChild("orderUid").equalTo(order.orderUid.toDouble()).get().addOnCompleteListener {
            for (dataSnapshot in it.result.children) {
                // 판매자용 앱에서는 일단 주문 상태만 변경
                dataSnapshot.ref.child("state").setValue(order.state)
            }
        }
    }
}