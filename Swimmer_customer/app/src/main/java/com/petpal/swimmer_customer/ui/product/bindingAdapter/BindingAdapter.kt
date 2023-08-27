package com.petpal.swimmer_customer.ui.product.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.productList
import com.petpal.swimmer_customer.ui.product.ProductViewModel


@BindingAdapter("productDetailImage")
fun imageUri(imageView: ImageView, imageUrl: String?) {

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val pathRef = storageRef.child(imageUrl!!)

    pathRef.downloadUrl.addOnSuccessListener {
        Glide.with(imageView.context)
            .load(it)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .error(R.drawable.noimg)
            .into(imageView)
    }
}


