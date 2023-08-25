package com.petpal.swimmer_seller.data.repository


import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.petpal.swimmer_seller.data.model.Item
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

    // 특정 판매자에게 들어온 모든 주문 가져오기
    fun getOrderBySellerUid(sellerUid: String, onOrdersLoaded: (List<Order>) -> Unit) {
        val orders = mutableListOf<Order>()
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children) {
                    val orderData = orderSnapshot.value as Map<*,*>
                    Log.d("orderData", orderData.toString())
                    val itemList = (orderData["itemList"] as List<*>).map {
                        val item = it as Map<*,*>
                        Item(
                            item["productUid"] as String,
                            //TODO Long으로 바꾸기
                            item["size"] as String,
                            //TODO Long으로 바꾸기
                            item["color"] as String,
                            item["quantity"] as Long,
                            item["sellerUid"] as String,
                            item["name"] as String,
                            item["mainImage"] as String,
                            item["price"] as Long
                        )
                    }
                    Log.d("order itemList", itemList.toString())
                    Log.d("order it.sellerUidm sellerUid", sellerUid)
                    if(itemList.any { it.sellerUid==sellerUid }) {
                        val order = Order(
                            orderSnapshot.key as String,
                            orderData["orderDate"] as String,
                            orderData["message"] as String,
                            //TODO: 나중에 Long으로 바꾸기
                            orderData["state"] as String,
                            //TODO: 나중에 Address으로 바꾸기
                            orderData["address"] as String,
                            itemList,
                            orderData["couponUid"] as String,
                            orderData["usePoint"] as Long,
                            //TODO: 나중에 Long으로 바꾸기
                            orderData["payMethod"] as String,
                            orderData["totalPrice"] as Long
                        )
                        Log.d("order myOrder", order.toString())
                        orders.add(order)
                    }
                }
                //필터링된 order가 들어감
                onOrdersLoaded(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                //빈 order가 들어감
                onOrdersLoaded(orders)
            }

        })
    }

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