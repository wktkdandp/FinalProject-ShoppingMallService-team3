package com.petpal.swimmer_customer.ui.product.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.ui.product.ProductViewModel


@BindingAdapter("productDetailImage")
fun imageUri(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(imageView.context)
            .load(it)
            .override(500, 10000)
            .error(R.drawable.noimg)
            .into(imageView)
    }
}

