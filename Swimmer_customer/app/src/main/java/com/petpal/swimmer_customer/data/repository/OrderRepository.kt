package com.petpal.swimmer_customer.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.petpal.swimmer_customer.data.model.ItemsForCustomer
import com.petpal.swimmer_customer.data.model.Order

class OrderRepository {


    private val ordersRef = FirebaseDatabase.getInstance().getReference("orders")

    fun getOrderByUserUid(userUid: String, onOrdersLoaded: (List<Order>) -> Unit) {

        val orders = mutableListOf<Order>()

        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderData = orderSnapshot.value as Map<*, *>
                    Log.d("orderData", orderData.toString())

                    if (orderData["userUid"] == userUid) {
                        val itemList = (orderData["itemList"] as List<*>).map {
                            val item = it as Map<*, *>
                            ItemsForCustomer(
                                item["productUid"] as String,
                                item["sellerUid"] as String,
                                item["name"] as String,
                                item["mainImage"] as String,
                                item["price"] as Long,
                                item["quantity"] as Long,
                                //TODO Long으로 바꾸기
                                item["size"] as String,
                                //TODO Long으로 바꾸기
                                item["color"] as String,

                                // 사용자 uid 정보 추가 8.29
                                item["buyerUid"] as String
                            )
                        }

                        val order = Order(
                            orderData["state"] as Long,
                            orderSnapshot.key as String,

                            // 주문자 uid 정보 추가 8.29
                            orderData["userUid"] as String,
                            orderData["orderDate"] as String,
                            orderData["message"] as String,
                            orderData["payMethod"] as Long,
                            orderData["totalPrice"] as Long,
                            itemList,
                            //TODO: 나중에 Address으로 바꾸기
                            orderData["address"] as String,
                            orderData["couponUid"] as String,
                            orderData["usePoint"] as Long,
                        )

                        orders.add(order)
                    }
                }
                Log.d("orderList", orders.toString())
                //필터링된 order가 들어감
                onOrdersLoaded(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                //빈 order가 들어감
                onOrdersLoaded(orders)
            }

        })
    }

}
