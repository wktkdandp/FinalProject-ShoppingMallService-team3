package com.petpal.swimmer_customer.ui.payment.repository

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

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

    }
}