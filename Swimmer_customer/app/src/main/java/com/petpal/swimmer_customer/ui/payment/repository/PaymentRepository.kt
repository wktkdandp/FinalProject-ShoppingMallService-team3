package com.petpal.swimmer_customer.ui.payment.repository

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.petpal.swimmer_customer.data.model.OrderByCustomer

class PaymentRepository {
    companion object {
        // item 가져오기
        fun getItems(callback: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val itemCodeRef = database.getReference("products")

            itemCodeRef.orderByChild("productUid").get().addOnCompleteListener(callback)
        }
        fun getCartItems(callback: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val itemCodeRef = database.getReference("products")

            itemCodeRef.orderByChild("productUid").get().addOnCompleteListener(callback)
        }

        // 이미지 firebaseStore를 통해 가져오기
        fun getItemImage(imageView: ImageView, fileName: String?) {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val pathRef = storageRef.child(fileName!!)

            pathRef.downloadUrl.addOnSuccessListener {
                Glide.with(imageView.context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(imageView)
            }
        }

        // customer -> seller 결제 완료 btn을 통해 주문 정보를 전송하는 메서드
        fun sendOrderToSeller(order: OrderByCustomer, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val orderRef = database.getReference("orders")

            orderRef.push().setValue(order).addOnCompleteListener(callback)
        }


    }
}